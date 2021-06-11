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
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.SizedBuilder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Double;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Literal;
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

    if (instance != null) {
      components.put(new JavaLanguageKey(IExpression.class, instance.getType()), new NullaryComponent<>(instance));
    }
    for (final Parameter parameter : parameters) {
      components.put(new JavaLanguageKey(IExpression.class, parameter.getType()), new NullaryComponent<>(parameter));
    }
    for (final FieldAccess field : fields) {
      final Type type = field.getType();
      final Component<JavaLanguageKey, FieldAccess> component = new NullaryComponent<>(field);
      components.put(new JavaLanguageKey(IExpression.class, type), component);
      components.put(new JavaLanguageKey(LeftHandSideExpression.class, type), component);
    }
    // TODO: Re-enable once fuzzing is smarter.
    // Assign.register(components, PrimitiveType.doubleType());
    Integer.register(components);
    Double.register(components);
    Invoke.register(components, methods);

    final Type[] types = { new VoidType(), PrimitiveType.doubleType(), PrimitiveType.intType(),
        TypeFactory.create(Calendar.class), TypeFactory.create(Date.class) };
    for (final Type type : types) {
      final JavaLanguageKey expressionKey = new JavaLanguageKey(IExpression.class, type);
      final JavaLanguageKey statementKey = new JavaLanguageKey(IStatement.class, type);
      components.put(statementKey, new ConstructorComponent<>(Arrays.asList(expressionKey), ExpressionStatement.class));
    }
    components.put(new JavaLanguageKey(IExpression.class, PrimitiveType.doubleType()),
        new NullaryComponent<>(new Literal(0d, PrimitiveType.doubleType())));
    components.put(new JavaLanguageKey(IExpression.class, PrimitiveType.intType()),
        new NullaryComponent<>(new Literal(0, PrimitiveType.intType())));
    components.put(new JavaLanguageKey(IExpression.class, PrimitiveType.intType()),
        new NullaryComponent<>(new Literal(11, PrimitiveType.intType())));
    components.put(new JavaLanguageKey(IExpression.class, new VoidType()),
        new NullaryComponent<>(new Literal(null, new VoidType())));
    components.put(new JavaLanguageKey(IExpression.class, TypeFactory.create(Calendar.class)),
        new NullaryComponent<>(new Literal(null, TypeFactory.create(Calendar.class))));
    components.put(new JavaLanguageKey(IExpression.class, TypeFactory.create(Date.class)),
        new NullaryComponent<>(new Literal(null, TypeFactory.create(Date.class))));

    statements = new SizedBuilder<>(components, MAX_STATEMENT_LENGTH);
  }

  @Override
  public SnippetCandidate generate(final SourceOfRandomness random, final GenerationStatus status) {
    final List<JavaLanguageKey> allTypes = Arrays.asList(new JavaLanguageKey(IStatement.class, new VoidType()),
        new JavaLanguageKey(IStatement.class, TypeFactory.create(int.class)),
        new JavaLanguageKey(IStatement.class, TypeFactory.create(double.class)),
        new JavaLanguageKey(IStatement.class, TypeFactory.create(Calendar.class)),
        new JavaLanguageKey(IStatement.class, TypeFactory.create(Date.class)));
    final JavaLanguageKey resultKey = new JavaLanguageKey(IStatement.class, TypeFactory.create(resultType));
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
        final JavaLanguageKey key = new JavaLanguageKey(IExpression.class, expression.getType());
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
