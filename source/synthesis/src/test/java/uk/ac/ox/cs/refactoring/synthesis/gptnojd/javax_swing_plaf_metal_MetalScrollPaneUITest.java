
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_metal_MetalScrollPaneUITest {
  @Test
  void uninstallListeners() throws Exception {
    assertThat(synthesiseGPT("this.uninstallListeners(a);\n\n", "MetalScrollPaneUI ui = (MetalScrollPaneUI) a.getUI();\nui.uninstallListeners(a);\n", "javax.swing.plaf.metal.MetalScrollPaneUI", "uninstallListeners", "javax.swing.JScrollPane"), anyOf(contains("uninstallListeners")));
  }
}