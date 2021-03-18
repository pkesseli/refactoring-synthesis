package uk.ac.ox.cs.refactoring.synthesis.fuzz;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.verification.CandidateTest;

/**
 * 
 */
public abstract class SyntheticFrameworkMethod extends FrameworkMethod {

  /**
   * 
   */
  public SyntheticFrameworkMethod() {
    super(getPlaceholder(null));
  }

  /**
   * @param counterexample
   * @return
   */
  @Fuzz
  private static Method getPlaceholder(final Counterexample counterexample) {
    try {
      return CandidateTest.class.getDeclaredMethod("getPlaceholder", Counterexample.class);
    } catch (final NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(e);
    }
  }
}
