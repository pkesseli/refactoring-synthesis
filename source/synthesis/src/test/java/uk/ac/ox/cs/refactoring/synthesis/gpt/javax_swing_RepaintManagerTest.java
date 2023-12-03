
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_RepaintManagerTest {
  @Test
  void addDirtyRegion() throws Exception {
assertThat (synthesiseGPT ("this.addDirtyRegion(param0, param1, param2, param3, param4);" , "\nRepaintManager.currentManager(this).addDirtyRegion(this, param1, param2, param3, param4);\n;" , "javax.swing.RepaintManager" , "addDirtyRegion" , "java.applet.Applet" , "int" , "int" , "int" , "int") , Matchers . anything ()) ;
  }
}
