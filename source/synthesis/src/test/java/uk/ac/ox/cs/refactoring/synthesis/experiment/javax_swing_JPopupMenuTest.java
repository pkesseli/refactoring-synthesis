
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JPopupMenuTest {
  @Test
  void getComponentAtIndex() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JPopupMenu", "getComponentAtIndex", "int"),
        contains("getComponent"));
  }
}
        