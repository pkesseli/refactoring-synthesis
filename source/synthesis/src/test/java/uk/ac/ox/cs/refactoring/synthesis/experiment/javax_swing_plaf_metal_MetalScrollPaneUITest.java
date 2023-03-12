
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_plaf_metal_MetalScrollPaneUITest {
  @Test
  void uninstallListeners() throws Exception {
    assertThat(synthesiseAlias("javax.swing.plaf.metal.MetalScrollPaneUI", "uninstallListeners", "javax.swing.JScrollPane"), anyOf(contains("uninstallListeners")));
  }
}