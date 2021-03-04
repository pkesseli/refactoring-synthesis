package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * 
 */
public class Parameter implements IExpression {

  /**
   * 
   */
  private final int index;

  /**
   * 
   */
  private final Type type;

  /**
   * @param index {@link #index}
   * @param type  {@link #type}
   */
  public Parameter(final int index, final Type type) {
    this.index = index;
    this.type = type;
  }

  /**
   * 
   */
  private static final String PARAMETER_NAME_PREFIX = "param";

  @Override
  public Expression toNode() {
    return new NameExpr(PARAMETER_NAME_PREFIX + index);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Object evaluate(final ExecutionContext context) {
    return context.State.Arguments[index];
  }
}
