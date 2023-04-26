
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JViewportTest {
  @Test
  void isBackingStoreEnabled() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JViewport", "isBackingStoreEnabled"), anyOf(contains("getScrollMode")));
  }

  @Test
  void setBackingStoreEnabled() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JViewport", "setBackingStoreEnabled", "boolean"), anyOf(contains("setScrollMode")));
  }
}