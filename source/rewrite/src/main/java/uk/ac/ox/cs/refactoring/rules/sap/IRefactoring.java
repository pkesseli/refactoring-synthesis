package uk.ac.ox.cs.refactoring.rules.sap;

import com.github.javaparser.ast.Node;

/**
 * @author pkesseli
 *
 */
public interface IRefactoring {
  /**
   * @param node
   * @return
   */
  boolean isApplicable(Node node);

  /**
   * @param node
   * @return
   */
  Node transform(Node node);
}
