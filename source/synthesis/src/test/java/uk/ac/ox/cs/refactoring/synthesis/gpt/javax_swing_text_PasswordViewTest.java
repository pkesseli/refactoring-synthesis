
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_PasswordViewTest {
  @Test
  void drawEchoCharacter() throws Exception {
assertThat (synthesiseGPT ("this.drawEchoCharacter(param0, param1, param2, param3);" , "" , "javax.swing.text.PasswordView" , "drawEchoCharacter" , "java.awt.Graphics" , "int" , "int" , "char") , anyOf (contains ("drawEchoCharacter"))) ;
  }

  @Test
  void drawSelectedText() throws Exception {
assertThat (synthesiseGPT ("this.drawSelectedText(param0, param1, param2, param3, param4);" , "" , "javax.swing.text.PasswordView" , "drawSelectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawSelectedText"))) ;
  }

  @Test
  void drawUnselectedText() throws Exception {
assertThat (synthesiseGPT ("this.drawUnselectedText(param0, param1, param2, param3, param4);" , "" , "javax.swing.text.PasswordView" , "drawUnselectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawUnselectedText"))) ;
  }
}
