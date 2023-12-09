
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
assertThat (synthesiseGPT ("this.drawLine(param0, param1, param2, param3);" , "\nparam1.drawLine(param2, param3, x2, y2); // x2 and y2 should be the ending coordinates of the line\n" , "javax.swing.text.PlainView" , "drawLine" , "int" , "java.awt.Graphics" , "int" , "int") , anyOf (contains ("drawLine"))) ;
  }

  @Test
  void drawSelectedText() throws Exception {
assertThat (synthesiseGPT ("this.drawSelectedText(param0, param1, param2, param3, param4);" , "" , "javax.swing.text.PlainView" , "drawSelectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawSelectedText"))) ;
  }

  @Test
  void drawUnselectedText() throws Exception {
assertThat (synthesiseGPT ("this.drawUnselectedText(param0, param1, param2, param3, param4);" , "\nthis.drawUnselectedText(param0, param1, param2, param3, param4);\n" , "javax.swing.text.PlainView" , "drawUnselectedText" , "java.awt.Graphics" , "int" , "int" , "int" , "int") , anyOf (contains ("drawUnselectedText"))) ;
  }
}
