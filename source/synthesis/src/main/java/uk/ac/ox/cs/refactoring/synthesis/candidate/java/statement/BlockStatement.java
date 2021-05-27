package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IStatement;

/**
 * Models a block statement in Java.
 */
public class BlockStatement implements IStatement {

  /**
   * Statements in the block.
   */
  public final List<IStatement> Statements = new ArrayList<>();

  @Override
  public Statement toNode() {
    return new BlockStmt(
        new NodeList<>(Statements.stream().map(IStatement::toNode).collect(Collectors.toCollection(NodeList::new))));
  }

  /**
   * Provides the index of the last statement, which is treated as the return
   * value of a block.
   * 
   * @return Index in {@link #Statements} indicating the last statement.
   */
  private int getLastIndex() {
    return Statements.size() - 1;
  }

  @Override
  public Object execute(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    if (Statements.isEmpty()) {
      return null;
    }

    final int lastIndex = getLastIndex();
    for (int i = 0; i < lastIndex; ++i) {
      Statements.get(i).execute(context);
    }
    return Statements.get(lastIndex).execute(context);
  }

  @Override
  public Optional<IExpression> getSymbolExpression() {
    if (Statements.isEmpty())
      return Optional.empty();
    return Statements.get(getLastIndex()).getSymbolExpression();
  }
}
