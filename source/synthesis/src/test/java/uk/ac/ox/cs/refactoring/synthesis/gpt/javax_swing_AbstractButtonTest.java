
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_AbstractButtonTest {
  @Test
  void getLabel() throws Exception {
assertThat (synthesiseGPT ("this.getLabel();" , "this.getText();" , "javax.swing.AbstractButton" , "getLabel") , anyOf (contains ("getText"))) ;
  }

  @Test
  void setLabel() throws Exception {
assertThat (synthesiseGPT ("this.setLabel(param0);" , "this.setText(param0);" , "javax.swing.AbstractButton" , "setLabel" , "java.lang.String") , anyOf (contains ("setText"))) ;
  }
}
