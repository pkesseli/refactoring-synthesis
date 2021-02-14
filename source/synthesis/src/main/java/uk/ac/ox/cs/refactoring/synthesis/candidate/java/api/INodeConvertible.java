package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import com.github.javaparser.ast.Node;

/**
 * Implementing classes can be converted to {@Node}, which is mainly used to
 * print elements as Java code.
 */
public interface INodeConvertible<T extends Node> {
  /**
   * @return {@link Node} representing {@code this}.
   */
  T toNode();
}
