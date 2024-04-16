
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_PasswordViewTest {
  @Test
  void drawEchoCharacter() throws Exception {
assertThat (synthesiseNeural ("drawEchoCharacter" , "this.drawEchoCharacter(param0, param1, param2, param3);" , "\nif (param0 instanceof Graphics2D) {\n    Graphics2D g2d = (Graphics2D) param0;\n    this.drawEchoCharacter(g2d, (float) param1, (float) param2, param3);\n} else {\n    throw new IllegalArgumentException(\"Graphics must be an instance of Graphics2D\");\n}\n" , "javax.swing.text.PasswordView" , "drawEchoCharacter" , "java.awt.Graphics" , "int" , "int" , "char") , anyOf (contains ("drawEchoCharacter"))) ;
  }

  @Test
  void drawSelectedText() throws Exception {
assertThat (synthesiseNeural ("drawSelectedText" , "this.drawSelectedText(param0, param1, param2, param3, param4);" , "\nif (param0 instanceof Graphics2D) {\n    Graphics2D g2d = (Graphics2D) param0;\n    this.drawSelectedText(g2d, (float) param1, (float) param2, param3, param4);\n} else {\n    throw new IllegalArgumentException(\"Graphics must be an instance of Graphics2D\");\n}\n" , "javax.swing.text.PasswordView" , "drawSelectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawSelectedText"))) ;
  }

  @Test
  void drawUnselectedText() throws Exception {
assertThat (synthesiseNeural ("drawUnselectedText" , "this.drawUnselectedText(param0, param1, param2, param3, param4);" , "\nif (param0 instanceof Graphics2D) {\n    Graphics2D g2d = (Graphics2D) param0;\n    this.drawUnselectedText(g2d, (float) param1, (float) param2, param3, param4);\n} else {\n    // handle error case\n}\n" , "javax.swing.text.PasswordView" , "drawUnselectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawUnselectedText"))) ;
  }
}
