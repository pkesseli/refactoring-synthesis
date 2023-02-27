package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/**
 * Literal expression with explicit type.
 */
public class Literal implements IExpression {

  /**
   * Wrapped literal expression.
   */
  private final Expression expression;

  /**
   * Type of literal.
   */
  private final Type type;

  /**
   * Java value of literal for evaluation.
   */
  private final Object value;

  /**
   * Generates an appropriate Java expression depending on the literal type.
   * 
   * @param value {@link #value}
   * @param type  {@link #type}
   */
  public Literal(final Object value, final Type type) {
    if (type.isPrimitiveType()) {
      switch (type.asPrimitiveType().getType()) {
        case BOOLEAN:
          expression = new BooleanLiteralExpr((boolean) value);
          break;
        case CHAR:
          expression = new CharLiteralExpr((char) value);
          break;
        case DOUBLE:
          expression = new DoubleLiteralExpr(value.toString());
          break;
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
          expression = new IntegerLiteralExpr(value.toString());
          break;
        default:
          throw new IllegalArgumentException();
      }
    } else {
      expression = new NullLiteralExpr();
    }
    this.type = type;
    this.value = value;
  }

  @Override
  public Expression toNode() {
    return expression;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Object evaluate(final ExecutionContext context) {
    return value;
  }

}
