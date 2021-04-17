package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
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

  @Override
  public Object execute(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    if (Statements.isEmpty()) {
      return null;
    }

    final int lastIndex = Statements.size() - 1;
    for (int i = 0; i < lastIndex - 1; ++i) {
      Statements.get(i).execute(context);
    }
    return Statements.get(lastIndex).execute(context);
  }
}
