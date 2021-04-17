package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import java.lang.reflect.InvocationTargetException;

import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;

/**
 * Expression statement in the Java language.
 */
public class ExpressionStatement implements IStatement {

  /**
   * Wrapped expression.
   */
  private final IExpression expression;

  /**
   * @param expression {@link #expression}
   */
  public ExpressionStatement(final IExpression expression) {
    this.expression = expression;
  }

  @Override
  public Statement toNode() {
    return new ExpressionStmt(expression.toNode());
  }

  @Override
  public Object execute(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    return expression.evaluate(context);
  }
}
