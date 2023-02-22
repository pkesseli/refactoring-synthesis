
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JInternalFrameTest {
  @Test
  void getMenuBar() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JInternalFrame", "getMenuBar"),
        contains("getJMenuBar"));
  }

  @Test
  void setMenuBar() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JInternalFrame", "setMenuBar", "javax.swing.JMenuBar"),
        contains("setJMenuBar"));
  }
}
