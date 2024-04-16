
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JMenuBarTest {
  @Test
  void getComponentAtIndex() throws Exception {
assertThat (synthesiseNeural ("getComponentAtIndex" , "this.getComponentAtIndex(param0);" , "\nthis.getComponent(param0);\n" , "javax.swing.JMenuBar" , "getComponentAtIndex" , "int") , anyOf (contains ("getComponent"))) ;
  }
}
