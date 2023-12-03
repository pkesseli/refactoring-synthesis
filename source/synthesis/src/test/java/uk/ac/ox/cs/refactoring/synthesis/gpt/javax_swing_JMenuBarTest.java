
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JMenuBarTest {
  @Test
  void getComponentAtIndex() throws Exception {
assertThat (synthesiseGPT ("this.getComponentAtIndex(param0);" , "this.getMenuComponent(param0);" , "javax.swing.JMenuBar" , "getComponentAtIndex" , "int") , anyOf (contains ("getComponent"))) ;
  }
}
