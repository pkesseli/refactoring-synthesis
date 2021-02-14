package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

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
  public void run() {
    expression.evaluate();
  }
}
