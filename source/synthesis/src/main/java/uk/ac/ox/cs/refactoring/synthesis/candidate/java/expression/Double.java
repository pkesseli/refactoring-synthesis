package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * 
 */
public final class Double {
  private Double() {
  }

  /**
   * 
   */
  private static abstract class BinaryExpression implements IExpression {

    /**
     * 
     */
    private final IExpression lhs;

    /**
     * 
     */
    private final Operator operator;

    /**
     * 
     */
    private final IExpression rhs;

    /**
     * 
     * @param lhs
     * @param operator
     * @param rhs
     */
    public BinaryExpression(final IExpression lhs, final Operator operator, final IExpression rhs) {
      this.lhs = lhs;
      this.operator = operator;
      this.rhs = rhs;
    }

    /**
     * 
     * @return
     */
    public double getLhs() {
      return (double) lhs.evaluate();
    }

    /**
     * 
     * @return
     */
    public double getRhs() {
      return (double) rhs.evaluate();
    }

    @Override
    public Type getType() {
      return PrimitiveType.doubleType();
    }

    @Override
    public Expression toNode() {
      return new BinaryExpr(lhs.toNode(), rhs.toNode(), operator);
    }
  }

  private static class Plus extends BinaryExpression {
    public Plus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.PLUS, rhs);
    }

    @Override
    public java.lang.Double evaluate() {
      return getLhs() + getRhs();
    }
  }

  private static class Minus extends BinaryExpression {
    public Minus(final IExpression lhs, final IExpression rhs) {
      super(lhs, Operator.MINUS, rhs);
    }

    @Override
    public java.lang.Double evaluate() {
      return getLhs() - getRhs();
    }
  }
}
