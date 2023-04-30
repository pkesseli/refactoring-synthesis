
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
    assertThat(synthesiseGPT("JMenuBar menuBar = this.getMenuBar();\n\n", "JMenuBar menuBar = this.getJMenuBar();\n", "javax.swing.JInternalFrame", "getMenuBar"), anyOf(contains("getJMenuBar")));
  }

  @Test
  void setMenuBar() throws Exception {
    assertThat(synthesiseGPT("this.setMenuBar(a);\n", "this.setJMenuBar(a);\n", "javax.swing.JInternalFrame", "setMenuBar", "javax.swing.JMenuBar"), anyOf(contains("setJMenuBar")));
  }
}