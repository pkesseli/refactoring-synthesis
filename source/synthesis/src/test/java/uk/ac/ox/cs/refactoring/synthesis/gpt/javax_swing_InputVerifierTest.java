
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_InputVerifierTest {
  @Test
  void shouldYieldFocus() throws Exception {
    assertThat(synthesiseGPT("this.shouldYieldFocus(a);\n\n", "InputVerifier.shouldYieldFocus(this, a);\n", "javax.swing.InputVerifier", "shouldYieldFocus", "javax.swing.JComponent"), anyOf(contains("shouldYieldFocus")));
  }
}