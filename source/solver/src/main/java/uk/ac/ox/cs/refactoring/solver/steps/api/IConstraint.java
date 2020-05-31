package uk.ac.ox.cs.refactoring.solver.steps.api;

import com.github.javaparser.ast.expr.Expression;

/**
 * @author pkesseli
 *
 */
public interface IConstraint {
  /**
   * @param expression
   * @return
   */
  boolean isApplicable(Expression expression);
}
