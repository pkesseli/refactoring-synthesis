package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;

/**
 * 
 */
public class ExpressionStatement implements IStatement, Runnable {

  /**
   * 
   */
  private final IExpression expression;

  /**
   * 
   * @param expression
   */
  public ExpressionStatement(final IExpression expression) {
    this.expression = expression;
  }

  @Override
  public Statement toNode() {
    return new ExpressionStmt(expression.toNode());
  }

  @Override
  public void run() {
    expression.evaluate();
  }
}
