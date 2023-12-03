
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_KeyStrokeTest {
  @Test
  void getKeyStroke() throws Exception {
assertThat (synthesiseGPT ("this.getKeyStroke(param0, param1);" , "KeyStroke.getKeyStroke(param0, param1);" , "javax.swing.KeyStroke" , "getKeyStroke" , "char" , "boolean") , Matchers . anything ()) ;
  }
}
