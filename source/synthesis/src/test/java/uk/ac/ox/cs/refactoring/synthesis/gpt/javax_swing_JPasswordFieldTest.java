
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JPasswordFieldTest {
  @Test
  void getText1() throws Exception {
assertThat (synthesiseGPT ("this.getText();" , "\nnew String(this.getPassword())\n;" , "javax.swing.JPasswordField" , "getText") , anyOf (contains ("getPassword"))) ;
  }

  @Test
  void getText2() throws Exception {
assertThat (synthesiseGPT ("this.getText(param0, param1);" , "\nthis.getPassword().subSequence(param0, param1).toString()\n;" , "javax.swing.JPasswordField" , "getText" , "int" , "int") , anyOf (contains ("getPassword"))) ;
  }
}
