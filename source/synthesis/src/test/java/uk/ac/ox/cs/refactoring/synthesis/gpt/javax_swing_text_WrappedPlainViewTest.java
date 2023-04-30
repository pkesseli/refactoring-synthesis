
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_WrappedPlainViewTest {
  @Test
  void drawLine() throws Exception {
    assertThat(synthesiseGPT("this.drawLine(a, b, c, d, e);\n", "Graphics2D g2d = (Graphics2D) c;\nthis.drawLine(a, b, g2d, (float) d, (float) e);\n", "javax.swing.text.WrappedPlainView", "drawLine", "int", "int", "java.awt.Graphics", "int", "int"), anyOf(contains("drawLine")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawSelectedText(a, b, c, d, e);\n", "Graphics2D g2d = (Graphics2D) a;\nthis.drawSelectedText(g2d, (float) b, (float) c, d, e);\n", "javax.swing.text.WrappedPlainView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawUnselectedText(a, b, c, d, e);\n", "((Graphics2D) a).setFont(getFont());\n((Graphics2D) a).setColor(getForeground());\n((Graphics2D) a).drawString(getText().substring(d, e), b, c);\n", "javax.swing.text.WrappedPlainView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}