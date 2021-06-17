package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Builder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.SizedBuilder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Double;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * {@link Generator} for Java {@link SnippetCandidate}.
 */
public class SnippetCandidateGenerator extends Generator<SnippetCandidate> {

  /**
   * Maximum number of statements per candidate.
   */
  private static final byte MAX_STATEMENTS = 3;

  /**
   * Maximum length of one statement in a candidate.
   */
  private static final byte MAX_STATEMENT_LENGTH = 3;

  /**
   * Minimum number of statements per candidate.
   */
  private static final byte MIN_STATEMENTS = 3;

  /**
   * {@code this} expression to use in generated snippets.
   */
  private final This instance;

  /**
   * Expected output type of the generated snippet.
   */
  private final Class<?> resultType;

  /**
   * Method prameters to use in generated snippets.
   */
  private final List<Parameter> parameters;

  /**
   * Field access expressions to use in generaed snippets.
   */
  private final List<FieldAccess> fields;

  /**
   * Methods which should be part of the instruction set.
   */
  private final Iterable<Method> methods;

  /**
   * Expressions and statements to be used to construct a candidate.
   */
  private final ComponentDirectory components = new ComponentDirectory();

  /**
   * Builder for single statements.
   */
  private final Builder<JavaLanguageKey, IStatement> statements;

  /**
   * @param instance   {@link #instance}
   * @param parameters {@link #parameters}
   * @param fields     {@link #fields}
   * @param methods
   */
  public SnippetCandidateGenerator(final This instance, final Class<?> resultType, final List<Parameter> parameters,
      final List<FieldAccess> fields, final Iterable<Method> methods) {
    super(SnippetCandidate.class);
    this.instance = instance;
    this.resultType = resultType;
    this.parameters = parameters;
    this.fields = fields;
    this.methods = methods;

    final JavaComponents components = new JavaComponents(this.components);
    if (instance != null)
      components.nonnull(instance.getType(), new NullaryComponent<>(instance));
    for (final Parameter parameter : parameters)
      components.nonnull(parameter.getType(), new NullaryComponent<>(parameter));
    for (final FieldAccess field : fields)
      components.lhs(field.getType(), new NullaryComponent<>(field));

    // TODO: Re-enable once fuzzing is smarter.
    // Assign.register(components, PrimitiveType.doubleType());
    Integer.register(components);
    Double.register(components);
    Invoke.register(components, methods);

    final Type[] types = { new VoidType(), PrimitiveType.doubleType(), PrimitiveType.intType(),
        TypeFactory.create(Calendar.class), TypeFactory.create(Date.class) };
    for (final Type type : types) {
      final JavaLanguageKey expressionKey = JavaLanguageKeys.expression(type);
      components.statement(type, new ConstructorComponent<>(Arrays.asList(expressionKey), ExpressionStatement.class));
    }

    components.literal(PrimitiveType.doubleType(), 0d);
    components.literal(PrimitiveType.intType(), 0);
    components.literal(PrimitiveType.intType(), 11);
    components.nullLiteral(new VoidType());
    components.nullLiteral(TypeFactory.create(Calendar.class));
    components.nullLiteral(TypeFactory.create(Date.class));

    statements = new SizedBuilder<>(this.components, MAX_STATEMENT_LENGTH);
  }

  @Override
  public SnippetCandidate generate(final SourceOfRandomness random, final GenerationStatus status) {
    final List<JavaLanguageKey> allTypes = Arrays.asList(JavaLanguageKeys.statement(new VoidType()),
        JavaLanguageKeys.statement(TypeFactory.create(int.class)),
        JavaLanguageKeys.statement(TypeFactory.create(double.class)),
        JavaLanguageKeys.statement(TypeFactory.create(Calendar.class)),
        JavaLanguageKeys.statement(TypeFactory.create(Date.class)));
    final JavaLanguageKey resultKey = JavaLanguageKeys.statement(TypeFactory.create(resultType));
    final ComponentDirectory temporaryVariables = new ComponentDirectory();
    final SnippetCandidate result = new SnippetCandidate();
    final byte size = random.nextByte(MIN_STATEMENTS, MAX_STATEMENTS);
    for (byte i = 0; i < size; ++i) {
      final List<JavaLanguageKey> allowedTypes;
      if (i == size - 1) {
        allowedTypes = Arrays.asList(resultKey);
      } else {
        allowedTypes = allTypes;
      }
      final IStatement statement = statements.build(random, allowedTypes, temporaryVariables);
      result.Block.Statements.add(statement);

      final Optional<IExpression> symbol = statement.getSymbolExpression();
      if (symbol.isPresent()) {
        final IExpression expression = symbol.get();
        final JavaLanguageKey key = JavaLanguageKeys.nonnull(expression.getType());
        temporaryVariables.put(key, new NullaryComponent<>(expression));
      }
    }

    return result;
  }

  @Override
  public Generator<SnippetCandidate> copy() {
    return new SnippetCandidateGenerator(instance, resultType, parameters, fields, methods);
  }

  public static class TestClass {
    /**
     * @param candidate
     * @return
     */
    @Fuzz
    public static Method getFrameworkMethodPlaceholder(final SnippetCandidate candidate) {
      try {
        return TestClass.class.getDeclaredMethod("getFrameworkMethodPlaceholder", SnippetCandidate.class);
      } catch (final NoSuchMethodException | SecurityException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
