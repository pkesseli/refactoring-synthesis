
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JListTest {
  @Test
  void getSelectedValues() throws Exception {
assertThat (synthesiseNeural ("getSelectedValues" , "this.getSelectedValues();" , "\nthis.getSelectedValuesList();\n" , "javax.swing.JList" , "getSelectedValues") , anyOf (contains ("getSelectedValuesList"))) ;
  }
}
