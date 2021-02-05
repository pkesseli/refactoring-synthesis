package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import com.github.javaparser.ast.Node;

/**
 * 
 */
public interface INodeConvertible<T extends Node> {
  /**
   * 
   * @return
   */
  T toNode();
}
