package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.SizedBuilder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Assign;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Double;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

/**
 * {@link Supplier} for Java {@link SnippetCandidate}.
 */
public class CandidateSupplier implements Supplier<SnippetCandidate> {

  /**
   * Maximum number of statements per candidate.
   */
  private static final byte MAX_STATEMENTS = 10;

  /**
   * Minimum number of statements per candidate.
   */
  private static final byte MIN_STATEMENTS = 1;

  /**
   * Expressions and statements to be used to construct a candidate.
   */
  private final ComponentDirectory components = new ComponentDirectory();

  /**
   * {@link SourceOfRandomness} guiding the construction of candidates.
   */
  private final SourceOfRandomness sourceOfRandomness;

  /**
   * Builder for single statements.
   */
  private final Supplier<IStatement> statements;

  /**
   * @param fields             {@link FieldAccess Field expressions} to use when
   *                           constructing Java snippets.
   * @param sourceOfRandomness {@link #sourceOfRandomness}
   */
  public CandidateSupplier(final List<FieldAccess> fields, final SourceOfRandomness sourceOfRandomness) {
    for (final FieldAccess field : fields) {
      final Type type = field.getType();
      final Component<JavaLanguageKey, FieldAccess> component = new NullaryComponent<>(field);
      components.put(new JavaLanguageKey(IExpression.class, type), component);
      components.put(new JavaLanguageKey(LeftHandSideExpression.class, type), component);
    }
    Assign.register(components, PrimitiveType.doubleType());
    Double.register(components);

    final JavaLanguageKey expressionKey = new JavaLanguageKey(IExpression.class, PrimitiveType.doubleType());
    final JavaLanguageKey statementKey = new JavaLanguageKey(IStatement.class, PrimitiveType.doubleType());
    components.put(statementKey, new ConstructorComponent<>(Arrays.asList(expressionKey), ExpressionStatement.class));

    this.sourceOfRandomness = sourceOfRandomness;
    statements = new SizedBuilder<>(components, MAX_STATEMENTS, statementKey, sourceOfRandomness);
  }

  @Override
  public SnippetCandidate get() {
    final SnippetCandidate result = new SnippetCandidate();
    final byte size = sourceOfRandomness.nextByte(MIN_STATEMENTS, MAX_STATEMENTS);
    for (byte i = 0; i < size; ++i) {
      result.Block.Statements.add(statements.get());
    }
    return result;
  }
}
