
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
assertThat (synthesiseGPT ("drawEchoCharacter" , "this.drawEchoCharacter(param0, param1, param2, param3);" , "\nfinal Graphics2D graphics2D = (Graphics2D) param0;\nfinal float x = (float) param1;\nfinal float y = (float) param2;\nfinal char echoChar = param3;\nthis.drawEchoCharacter(graphics2D, x, y, echoChar);\n" , "javax.swing.text.PasswordView" , "drawEchoCharacter" , "java.awt.Graphics" , "int" , "int" , "char") , anyOf (contains ("drawEchoCharacter"))) ;
  }

  @Test
  void drawSelectedText() throws Exception {
assertThat (synthesiseGPT ("drawSelectedText" , "this.drawSelectedText(param0, param1, param2, param3, param4);" , "\nif (param0 instanceof Graphics2D) {\n    final Graphics2D graphics2D = (Graphics2D) param0;\n    final float x = (float) param1;\n    final float y = (float) param2;\n    final int start = param3;\n    final int end = param4;\n    this.drawSelectedText(graphics2D, x, y, start, end);\n} else {\n    throw new IllegalArgumentException(\"Graphics must be an instance of Graphics2D\");\n}\n" , "javax.swing.text.PasswordView" , "drawSelectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawSelectedText"))) ;
  }

  @Test
  void drawUnselectedText() throws Exception {
assertThat (synthesiseGPT ("drawUnselectedText" , "this.drawUnselectedText(param0, param1, param2, param3, param4);" , "\nfinal Graphics2D graphics2D = (Graphics2D) param0;\nthis.drawUnselectedText(graphics2D, (float) param1, (float) param2, param3, param4);\n" , "javax.swing.text.PasswordView" , "drawUnselectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawUnselectedText"))) ;
  }
}
