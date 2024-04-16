
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JPasswordFieldTest {
  @Test
  void getText1() throws Exception {
assertThat (synthesiseNeural ("getText1" , "this.getText();" , "\nnew String(this.getPassword());\n" , "javax.swing.JPasswordField" , "getText") , anyOf (contains ("getPassword"))) ;
  }

  @Test
  void getText2() throws Exception {
assertThat (synthesiseNeural ("getText2" , "this.getText(param0, param1);" , "\nthis.getPassword().toString().substring(param0, param1);\n" , "javax.swing.JPasswordField" , "getText" , "int" , "int") , anyOf (contains ("getPassword"))) ;
  }
}
