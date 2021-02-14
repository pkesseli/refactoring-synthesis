package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

/**
 * Models a Java language left-hand-side expression.
 */
public interface LeftHandSideExpression extends IExpression {
  /**
   * @param value If evaluated in an assignment expression as a left-hand-side
   *              expression, this operation will be invoked.
   */
  void set(Object value);
}
