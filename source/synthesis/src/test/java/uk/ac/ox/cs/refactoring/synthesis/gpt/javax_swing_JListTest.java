
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JListTest {
  @Test
  void getSelectedValues() throws Exception {
assertThat (synthesiseGPT ("getSelectedValues" , "this.getSelectedValues();" , "\nthis.getSelectedValuesList().toArray(new Object[0])\n;" , "javax.swing.JList" , "getSelectedValues") , anyOf (contains ("getSelectedValuesList"))) ;
  }
}
