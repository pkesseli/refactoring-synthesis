
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JMenuBarTest {
  @Test
  void getComponentAtIndex() throws Exception {
    assertThat(synthesiseGPT("JMenuBar menuBar = new JMenuBar();\nComponent component = menuBar.getComponentAtIndex(a);\n\n", "JMenuBar menuBar = new JMenuBar();\nComponent component = menuBar.getMenuComponent(a);\n", "javax.swing.JMenuBar", "getComponentAtIndex", "int"), anyOf(contains("getComponent")));
  }
}