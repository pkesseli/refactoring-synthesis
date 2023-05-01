
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
    assertThat(synthesiseGPT("this.drawLine(a, b, c, d);\n\n", "Graphics2D g2d = (Graphics2D) b;\nthis.drawLine(a, g2d, c, d);\n", "javax.swing.text.PlainView", "drawLine", "int", "java.awt.Graphics", "int", "int"), anyOf(contains("drawLine")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawSelectedText(a, b, c, d, e);\n", "Graphics2D g2d = (Graphics2D) a;\nthis.drawSelectedText(g2d, (float) b, (float) c, d, e);\n", "javax.swing.text.PlainView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawUnselectedText(a, b, c, d, e);\n", "Graphics2D g2d = (Graphics2D) a;\nthis.drawUnselectedText(g2d, (float) b, (float) c, d, e);\n", "javax.swing.text.PlainView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}