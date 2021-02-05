package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.util.function.Supplier;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

/**
 * 
 */
public class CandidateSupplier implements Supplier<SnippetCandidate> {

  /**
   * 
   */
  private static final byte MAX_STATEMENTS = 10;

  /**
   * 
   */
  private static final byte MIN_STATEMENTS = 1;

  /**
   * 
   */
  private final SourceOfRandomness sourceOfRandomness;

  /**
   * 
   * @param sourceOfRandomness
   */
  public CandidateSupplier(SourceOfRandomness sourceOfRandomness) {
    this.sourceOfRandomness = sourceOfRandomness;
  }

  @Override
  public SnippetCandidate get() {
    final SnippetCandidate result = new SnippetCandidate();
    final byte size = sourceOfRandomness.nextByte(MIN_STATEMENTS, MAX_STATEMENTS);
    for (byte i = 0; i < size; ++i) {
      result.Statements.add(getStatement());
    }
    return result;
  }

  /**
   * 
   * @return
   */
  private IExpression getExpression() {
    // TODO: Make this configurable
    return null;
  }

  /**
   * 
   * @return
   */
  private IStatement getStatement() {
    return new ExpressionStatement(getExpression());
  }
}
