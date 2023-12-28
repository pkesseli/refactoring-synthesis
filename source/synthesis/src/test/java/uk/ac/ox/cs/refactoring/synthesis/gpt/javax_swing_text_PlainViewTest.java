
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
assertThat (synthesiseGPT ("drawLine" , "this.drawLine(param0, param1, param2, param3);" , "\nfinal Graphics2D graphics2D = (Graphics2D) param1;\nfinal float x1 = (float) this.modelToView(param0).getBounds().getX();\nfinal float y1 = (float) this.modelToView(param0).getBounds().getY();\nfinal float x2 = (float) this.modelToView(param2).getBounds().getX();\nfinal float y2 = (float) this.modelToView(param2).getBounds().getY();\nthis.drawLine(graphics2D, x1, y1, x2, y2);\n" , "javax.swing.text.PlainView" , "drawLine" , "int" , "java.awt.Graphics" , "int" , "int") , anyOf (contains ("drawLine"))) ;
  }

  @Test
  void drawSelectedText() throws Exception {
assertThat (synthesiseGPT ("drawSelectedText" , "this.drawSelectedText(param0, param1, param2, param3, param4);" , "\nfinal Graphics2D graphics2D = (Graphics2D) param0;\nthis.drawSelectedText(graphics2D, param1, param2, param3, param4);\n" , "javax.swing.text.PlainView" , "drawSelectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawSelectedText"))) ;
  }

  @Test
  void drawUnselectedText() throws Exception {
assertThat (synthesiseGPT ("drawUnselectedText" , "this.drawUnselectedText(param0, param1, param2, param3, param4);" , "\nfinal Graphics2D graphics2D = (Graphics2D) param0;\nthis.drawUnselectedText(graphics2D, param1, param2, param3, param4);\n" , "javax.swing.text.PlainView" , "drawUnselectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawUnselectedText"))) ;
  }
}
