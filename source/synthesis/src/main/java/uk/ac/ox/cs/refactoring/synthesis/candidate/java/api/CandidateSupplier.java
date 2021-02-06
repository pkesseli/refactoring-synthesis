package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Supplier;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.HierarchicalReflectiveComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.SizedBuilder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Assign;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Double;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

/**
 * 
 */
public class CandidateSupplier implements Supplier<SnippetCandidate> {

  /**
   * 
   */
  private static final byte MAX_STATEMENTS = 10;

  /**
   * 
   */
  private static final byte MIN_STATEMENTS = 1;

  /**
   * 
   */
  private final ComponentDirectory components = new ComponentDirectory();

  /**
  * 
  */
  private final SourceOfRandomness sourceOfRandomness;

  /**
   * 
   */
  private final SizedBuilder<IStatement> statements;

  /**
   * 
   * @param sourceOfRandomness
   */
  public CandidateSupplier(final List<FieldAccess> fields, final SourceOfRandomness sourceOfRandomness) {
    for (final FieldAccess field : fields) {
      final Component<FieldAccess> component = new NullaryComponent<>(field);
      components.put(IExpression.class, component);
      components.put(LeftHandSideExpression.class, component);
    }
    components.put(IStatement.class, new HierarchicalReflectiveComponent<>(ExpressionStatement.class));
    registerMembers(Assign.class);
    registerMembers(Double.class);

    this.sourceOfRandomness = sourceOfRandomness;
    statements = new SizedBuilder<>(components, MAX_STATEMENTS, IStatement.class, sourceOfRandomness);
  }

  @Override
  public SnippetCandidate get() {
    final SnippetCandidate result = new SnippetCandidate();
    final byte size = sourceOfRandomness.nextByte(MIN_STATEMENTS, MAX_STATEMENTS);
    for (byte i = 0; i < size; ++i) {
      result.Statements.add(statements.get());
    }
    return result;
  }

  private void registerMembers(final Class<?> parent) {
    for (final Class<?> cls : parent.getDeclaredClasses()) {
      if (Modifier.isAbstract(cls.getModifiers())) {
        continue;
      }

      @SuppressWarnings("unchecked")
      final Class<? extends IExpression> converted = (Class<? extends IExpression>) cls;
      components.put(IExpression.class, new HierarchicalReflectiveComponent<>(converted));
    }
  }
}
