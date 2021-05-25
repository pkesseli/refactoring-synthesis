package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * Java {@code this} expression.
 */
public class This implements IExpression {

  /**
   * Type of the instance.
   */
  private final Type type;

  /**
   * @param type {@link #type}
   */
  private This(final Type type) {
    this.type = type;
  }

  /**
   * @param type {@link This}
   * @return {@link This}
   */
  public static This create(final Type type) {
    if (type == null)
      return null;
    return new This(type);
  }

  @Override
  public Expression toNode() {
    return new ThisExpr();
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Object evaluate(final ExecutionContext context) {
    return context.State.Instance;
  }
}
