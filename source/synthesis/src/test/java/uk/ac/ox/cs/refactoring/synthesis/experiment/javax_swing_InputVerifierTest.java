
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_InputVerifierTest {
  @Test
  void shouldYieldFocus() throws Exception {
    assertThat(synthesiseAlias("javax.swing.InputVerifier", "shouldYieldFocus", "javax.swing.JComponent"), anyOf(contains("shouldYieldFocus")));
  }
}