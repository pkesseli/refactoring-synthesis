package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * 
 */
public class This implements IExpression {

  /**
   * 
   */
  private final Type type;

  /**
   * @param type {@link #type}
   */
  public This(final Type type) {
    this.type = type;
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
