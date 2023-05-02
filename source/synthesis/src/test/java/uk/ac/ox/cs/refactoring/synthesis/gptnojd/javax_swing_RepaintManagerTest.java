
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_RepaintManagerTest {
  @Test
  void addDirtyRegion() throws Exception {
    assertThat(synthesiseGPT("this.addDirtyRegion(a, b, c, d, e);\n", "RepaintManager.currentManager(this).addDirtyRegion(this, b, c, d, e);\n", "javax.swing.RepaintManager", "addDirtyRegion", "java.applet.Applet", "int", "int", "int", "int"), Matchers.anything());
  }
}