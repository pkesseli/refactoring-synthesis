package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.SymbolExpression;

/**
 * Expression statement in the Java language.
 */
public class ExpressionStatement implements IStatement {

  /**
   * Wrapped expression.
   */
  private final IExpression expression;

  /**
   * Holds the value of the expression once evaluated. Each
   * {@link ExpressionStatement} models an implicit local variable holding its
   * expression's value for later use.
   */
  private Object value;

  /**
   * @param expression {@link #expression}
   */
  public ExpressionStatement(final IExpression expression) {
    this.expression = expression;
  }

  @Override
  public Statement toNode() {
    final Type type = expression.getType();
    final Expression node = expression.toNode();
    if (type.isVoidType()) {
      return new ExpressionStmt(node);
    }

    final VariableDeclarationExpr declare = new VariableDeclarationExpr(type, getLocalVariableName());
    final AssignExpr assign = new AssignExpr(declare, node, Operator.ASSIGN);
    return new ExpressionStmt(assign);
  }

  @Override
  public Object execute(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    return value = expression.evaluate(context);
  }

  @Override
  public Optional<IExpression> getSymbolExpression() {
    final Type type = expression.getType();
    if (type.isVoidType())
      return Optional.empty();

    return Optional.of(new SymbolExpression(getLocalVariableName(), type, this::getCurrentValue));
  }

  /**
   * Name of the implicit local variable generated.
   * 
   * @return Implicit local variable name.
   */
  private String getLocalVariableName() {
    return "tmp" + hashCode();
  }

  /**
   * Provides the value assigned to the explicit local variable modelled.
   * 
   * @return Value of expression assigned to local variable.
   */
  private Object getCurrentValue() {
    return value;
  }
}
