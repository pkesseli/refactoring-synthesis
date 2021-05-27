package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.BlockStatement;

/**
 * Java synthesis candidate representing a snippet.
 */
public class SnippetCandidate {
  /**
   * Block of statements synthesised.
   */
  public final BlockStatement Block = new BlockStatement();

  @Override
  public String toString() {
    return Block.toNode().toString();
  }
}
