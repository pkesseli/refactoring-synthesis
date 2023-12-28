
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_FocusManagerTest {
  @Test
  void disableSwingFocusManager() throws Exception {
assertThat (synthesiseGPT ("disableSwingFocusManager" , "this.disableSwingFocusManager();" , "\nKeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(param0);\n" , "javax.swing.FocusManager" , "disableSwingFocusManager") , anyOf (contains ("setDefaultFocusTraversalPolicy"))) ;
  }

  @Test
  void isFocusManagerEnabled() throws Exception {
assertThat (synthesiseGPT ("isFocusManagerEnabled" , "this.isFocusManagerEnabled();" , "\nKeyboardFocusManager.getDefaultFocusTraversalPolicy();\n" , "javax.swing.FocusManager" , "isFocusManagerEnabled") , anyOf (contains ("getDefaultFocusTraversalPolicy"))) ;
  }
}
