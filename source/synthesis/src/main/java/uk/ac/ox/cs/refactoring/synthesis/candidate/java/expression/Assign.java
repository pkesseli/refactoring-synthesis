package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.LeftHandSideExpression;

/**
 * 
 */
public final class Assign {

  /**
   * 
   */
  public static class Value implements IExpression {
    /**
     * 
     */
    private final LeftHandSideExpression lhs;

    /**
     * 
     */
    private final IExpression rhs;

    /**
     * 
     * @param lhs {@link #lhs}
     * @param rhs {@link #rhs}
     */
    public Value(final LeftHandSideExpression lhs, final IExpression rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public Expression toNode() {
      return new AssignExpr(lhs.toNode(), rhs.toNode(), Operator.ASSIGN);
    }

    @Override
    public Type getType() {
      return lhs.getType();
    }

    @Override
    public Object evaluate() {
      lhs.set(rhs.evaluate());
      return lhs.evaluate();
    }
  }
}
