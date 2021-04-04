package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.SizedBuilder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Double;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

/**
 * {@link Generator} for Java {@link SnippetCandidate}.
 */
public class SnippetCandidateGenerator extends Generator<SnippetCandidate> {

  /**
   * Maximum number of statements per candidate.
   */
  private static final byte MAX_STATEMENTS = 10;

  /**
   * Maximum length of one statement in a candidate.
   */
  private static final byte MAX_STATEMENT_LENGTH = 10;

  /**
   * Minimum number of statements per candidate.
   */
  private static final byte MIN_STATEMENTS = 1;

  /**
   * {@code this} expression to use in generated snippets.
   */
  private final This instance;

  /**
   * Method prameters to use in generated snippets.
   */
  private final List<Parameter> parameters;

  /**
   * Field access expressions to use in generaed snippets.
   */
  private final List<FieldAccess> fields;

  /**
   * Expressions and statements to be used to construct a candidate.
   */
  private final ComponentDirectory components = new ComponentDirectory();

  /**
   * Builder for single statements.
   */
  private final Function<SourceOfRandomness, IStatement> statements;

  /**
   * @param instance   {@link #instance}
   * @param parameters {@link #parameters}
   * @param fields     {@link #fields}
   */
  public SnippetCandidateGenerator(final This instance, final List<Parameter> parameters,
      final List<FieldAccess> fields) {
    super(SnippetCandidate.class);
    this.instance = instance;
    this.parameters = parameters;
    this.fields = fields;

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
    Double.register(components);

    final JavaLanguageKey expressionKey = new JavaLanguageKey(IExpression.class, PrimitiveType.doubleType());
    final JavaLanguageKey statementKey = new JavaLanguageKey(IStatement.class, PrimitiveType.doubleType());
    components.put(statementKey, new ConstructorComponent<>(Arrays.asList(expressionKey), ExpressionStatement.class));

    statements = new SizedBuilder<>(components, MAX_STATEMENT_LENGTH, statementKey);
  }

  @Override
  public SnippetCandidate generate(final SourceOfRandomness random, final GenerationStatus status) {
    final SnippetCandidate result = new SnippetCandidate();
    final byte size = random.nextByte(MIN_STATEMENTS, MAX_STATEMENTS);
    for (byte i = 0; i < size; ++i) {
      result.Block.Statements.add(statements.apply(random));
    }
    return result;
  }

  @Override
  public Generator<SnippetCandidate> copy() {
    return new SnippetCandidateGenerator(instance, parameters, fields);
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
