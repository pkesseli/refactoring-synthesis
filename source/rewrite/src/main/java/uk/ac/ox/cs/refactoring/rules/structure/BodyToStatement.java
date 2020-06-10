package uk.ac.ox.cs.refactoring.rules.structure;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;

import uk.ac.ox.cs.refactoring.rules.sap.IRefactoring;

/**
 * @author pkesseli
 *
 */
public class BodyToStatement implements IRefactoring {

  @Override
  public boolean isApplicable(final Node node) {
    if (!(node instanceof BlockStmt))
      return false;

    final BlockStmt block = (BlockStmt) node;
    return block.getStatements().size() == 1;
  }

  @Override
  public Node transform(final Node node) {
    return ((BlockStmt) node).getStatement(0);
  }
}
