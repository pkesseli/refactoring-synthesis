
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_JComponentTest {
  @Test
  @Disabled("Change of signature")
  void reshape() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "reshape", "int", "int", "int", "int"),
        contains("setBounds"));
  }
}
