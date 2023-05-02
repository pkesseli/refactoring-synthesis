
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JInternalFrameTest {
  @Test
  void getMenuBar() throws Exception {
    assertThat(synthesiseGPT("JMenuBar menuBar = this.getMenuBar();\n\n", "JMenuBar menuBar = null;\nJMenuBar[] menuBars = this.getJMenuBar();\nif (menuBars.length > 0) {\n    menuBar = menuBars[0];\n}\n", "javax.swing.JInternalFrame", "getMenuBar"), anyOf(contains("getJMenuBar")));
  }

  @Test
  void setMenuBar() throws Exception {
    assertThat(synthesiseGPT("JMenuBar a = new JMenuBar();\nthis.setMenuBar(a);\n\n", "JMenuBar a = new JMenuBar();\nJMenu menu = new JMenu(\"Menu\");\nJMenuItem menuItem = new JMenuItem(\"MenuItem\");\nmenu.add(menuItem);\na.add(menu);\nthis.setJMenuBar(a);\n", "javax.swing.JInternalFrame", "setMenuBar", "javax.swing.JMenuBar"), anyOf(contains("setJMenuBar")));
  }
}