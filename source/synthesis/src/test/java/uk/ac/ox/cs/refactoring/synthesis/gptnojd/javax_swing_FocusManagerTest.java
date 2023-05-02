
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_FocusManagerTest {
  @Test
  void disableSwingFocusManager() throws Exception {
    assertThat(synthesiseGPT("this.disableSwingFocusManager();\n", "FocusManager.getCurrentManager().setFocusTraversalPolicyProvider(true);\n", "javax.swing.FocusManager", "disableSwingFocusManager"), anyOf(contains("setDefaultFocusTraversalPolicy")));
  }

  @Test
  void isFocusManagerEnabled() throws Exception {
    assertThat(synthesiseGPT("this.isFocusManagerEnabled();\n\n", "this.getFocusTraversalPolicy().getDefaultComponent(this) != null;\n", "javax.swing.FocusManager", "isFocusManagerEnabled"), anyOf(contains("getDefaultFocusTraversalPolicy")));
  }
}