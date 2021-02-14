package uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

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
  public void run() {
    for (final IStatement statement : Statements) {
      statement.run();
    }
  }
}
