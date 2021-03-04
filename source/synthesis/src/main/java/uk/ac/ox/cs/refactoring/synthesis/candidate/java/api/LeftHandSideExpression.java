package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;

/**
 * Models a Java language left-hand-side expression.
 */
public interface LeftHandSideExpression extends IExpression {
  /**
   * @param context {@link ExecutionContext} in which to evaluate the expression.
   * @param value   If evaluated in an assignment expression as a left-hand-side
   *                expression, this operation will be invoked.
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  void set(ExecutionContext context, Object value)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException;
}
