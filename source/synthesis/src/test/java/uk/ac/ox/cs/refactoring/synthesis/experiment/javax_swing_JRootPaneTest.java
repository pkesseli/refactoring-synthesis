
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JRootPaneTest {
  @Test
  void getMenuBar() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JRootPane", "getMenuBar"), contains("getJMenuBar"));
  }

  @Test
  void setMenuBar() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JRootPane", "setMenuBar", "javax.swing.JMenuBar"),
        contains("setJMenuBar"));
  }
}
        