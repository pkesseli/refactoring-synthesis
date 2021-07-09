package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * Base for binary expressions.
 */
public abstract class BinaryExpression<T> implements IExpression {
  /**
   * Left-hand-side.
   */
  private final IExpression lhs;

  /**
   * Double {@link Operator}.
   */
  private final Operator operator;

  /**
   * Right-hand-side.
   */
  private final IExpression rhs;

  /**
   * Type of the expression.
   */
  private final Type type;

  /**
   * @param lhs      {@link #lhs}
   * @param operator {@link #operator}
   * @param rhs      {@link #rhs}
   * @param type     {@link #type}
   */
  public BinaryExpression(final IExpression lhs, final Operator operator, final IExpression rhs, final Type type) {
    this.lhs = lhs;
    this.operator = operator;
    this.rhs = rhs;
    this.type = type;
  }

  /**
   * @param context {@link IExpression#evaluate(ExecutionContext)}
   * @return Left-hand-side double value.
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  @SuppressWarnings("unchecked")
  public T getLhs(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    return (T) lhs.evaluate(context);
  }

  /**
   * @param context {@link IExpression#evaluate(ExecutionContext)}
   * @return Right-hand-side double value.
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  @SuppressWarnings("unchecked")
  public T getRhs(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    return (T) rhs.evaluate(context);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Expression toNode() {
    return new BinaryExpr(lhs.toNode(), rhs.toNode(), operator);
  }
}
