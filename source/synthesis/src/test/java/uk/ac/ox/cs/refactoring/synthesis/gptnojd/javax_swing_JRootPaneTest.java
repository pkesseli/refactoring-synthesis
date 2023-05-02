
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JRootPaneTest {
  @Test
  void getMenuBar() throws Exception {
    assertThat(synthesiseGPT("JMenuBar menuBar = this.getMenuBar();\n\n", "JMenuBar menuBar = JRootPane.class.cast(this.getRootPane()).getJMenuBar();\n", "javax.swing.JRootPane", "getMenuBar"), anyOf(contains("getJMenuBar")));
  }

  @Test
  void setMenuBar() throws Exception {
    assertThat(synthesiseGPT("this.setMenuBar(a);\n\n", "this.setJMenuBar(a);\n", "javax.swing.JRootPane", "setMenuBar", "javax.swing.JMenuBar"), anyOf(contains("setJMenuBar")));
  }
}