
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
assertThat (synthesiseGPT ("disableSwingFocusManager" , "this.disableSwingFocusManager();" , "\nContainer container = ...; // your container, e.g., a JPanel\nFocusTraversalPolicy myFTP = ...; // your custom FocusTraversalPolicy\ncontainer.setFocusTraversalPolicy(myFTP);\n```\n\nIf you simply want to remove the call to `disableSwingFocusManager()` because it's deprecated and you don't need to replace its functionality with anything specific, you can just delete the line:\n\n```java\n// this.disableSwingFocusManager(); // Deprecated, remove this line\n" , "javax.swing.FocusManager" , "disableSwingFocusManager") , anyOf (contains ("setDefaultFocusTraversalPolicy"))) ;
  }

  @Test
  void isFocusManagerEnabled() throws Exception {
assertThat (synthesiseGPT ("isFocusManagerEnabled" , "this.isFocusManagerEnabled();" , "\nthis.isFocusOwner()\n```\n\nIf you need to perform more complex focus-related checks or operations, you would use the `KeyboardFocusManager` class directly. For example:\n\n```java\nKeyboardFocusManager.getCurrentKeyboardFocusManager()\n;" , "javax.swing.FocusManager" , "isFocusManagerEnabled") , anyOf (contains ("getDefaultFocusTraversalPolicy"))) ;
  }
}
