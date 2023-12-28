
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JInternalFrameTest {
  @Test
  void getMenuBar() throws Exception {
assertThat (synthesiseGPT ("getMenuBar" , "this.getMenuBar();" , "\nthis.getJMenuBar();\n" , "javax.swing.JInternalFrame" , "getMenuBar") , anyOf (contains ("getJMenuBar"))) ;
  }

  @Test
  void setMenuBar() throws Exception {
assertThat (synthesiseGPT ("setMenuBar" , "this.setMenuBar(param0);" , "\nthis.setJMenuBar(param0);\n" , "javax.swing.JInternalFrame" , "setMenuBar" , "javax.swing.JMenuBar") , anyOf (contains ("setJMenuBar"))) ;
  }
}
