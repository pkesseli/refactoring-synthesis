
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_metal_MetalScrollPaneUITest {
  @Test
  void uninstallListeners() throws Exception {
assertThat (synthesiseGPT ("this.uninstallListeners(param0);" , "" , "javax.swing.plaf.metal.MetalScrollPaneUI" , "uninstallListeners" , "javax.swing.JScrollPane") , anyOf (contains ("uninstallListeners"))) ;
  }
}
