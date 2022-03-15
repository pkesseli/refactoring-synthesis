package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

abstract class CastExpression<T> implements IExpression {

  private final IExpression operand;

  /** Type of the expression. */
  private final Type type;

  /**
   * @param operand  {@link #operand}
   * @param operand  {@link #type}
   */
  CastExpression(final IExpression operand, final Type type) {
    this.operand = operand;
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  protected T evaluateOperand(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    return (T) operand.evaluate(context);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Expression toNode() {
    return new CastExpr(type, operand.toNode());
  }
}
