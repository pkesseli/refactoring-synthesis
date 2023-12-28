
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_PlainViewTest {
  @Test
  void drawLine() throws Exception {
assertThat (synthesiseGPT ("drawLine" , "this.drawLine(param0, param1, param2, param3);" , "\nGraphics2D g2d = (Graphics2D) param1;\nthis.drawLine(param0, g2d, param2, param3);\n" , "javax.swing.text.PlainView" , "drawLine" , "int" , "java.awt.Graphics" , "int" , "int") , anyOf (contains ("drawLine"))) ;
  }

  @Test
  void drawSelectedText() throws Exception {
assertThat (synthesiseGPT ("drawSelectedText" , "this.drawSelectedText(param0, param1, param2, param3, param4);" , "\nif (param0 instanceof Graphics2D) {\n    Graphics2D g2d = (Graphics2D) param0;\n    this.drawSelectedText(g2d, (float) param1, (float) param2, param3, param4);\n} else {\n    throw new IllegalArgumentException(\"Graphics must be an instance of Graphics2D\");\n}\n" , "javax.swing.text.PlainView" , "drawSelectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawSelectedText"))) ;
  }

  @Test
  void drawUnselectedText() throws Exception {
assertThat (synthesiseGPT ("drawUnselectedText" , "this.drawUnselectedText(param0, param1, param2, param3, param4);" , "\nif (param0 instanceof Graphics2D) {\n    Graphics2D g2d = (Graphics2D) param0;\n    this.drawUnselectedText(g2d, (float) param1, (float) param2, param3, param4);\n} else {\n    throw new IllegalArgumentException(\"Graphics must be an instance of Graphics2D\");\n}\n" , "javax.swing.text.PlainView" , "drawUnselectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawUnselectedText"))) ;
  }
}
