
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_SwingUtilitiesTest {
  @Test
  void findFocusOwner() throws Exception {
assertThat (synthesiseGPT ("findFocusOwner" , "this.findFocusOwner(param0);" , "\nKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();\n" , "javax.swing.SwingUtilities" , "findFocusOwner" , "java.awt.Component") , anyOf (contains ("getFocusOwner"))) ;
  }
}
