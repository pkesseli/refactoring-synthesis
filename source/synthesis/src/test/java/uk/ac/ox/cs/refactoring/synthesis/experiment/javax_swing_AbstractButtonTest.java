
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_AbstractButtonTest {
  @Test
  void getLabel() throws Exception {
    assertThat(synthesiseAlias("javax.swing.AbstractButton", "getLabel"),
        contains("getText"));
  }

  @Test
  void setLabel() throws Exception {
    assertThat(synthesiseAlias("javax.swing.AbstractButton", "setLabel", "java.lang.String"),
        contains("setText"));
  }
}
