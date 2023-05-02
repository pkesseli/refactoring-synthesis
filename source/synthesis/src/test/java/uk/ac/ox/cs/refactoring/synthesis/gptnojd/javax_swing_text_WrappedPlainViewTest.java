
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_WrappedPlainViewTest {
  @Test
  void drawLine() throws Exception {
    assertThat(synthesiseGPT("this.drawLine(a, b, c, d, e);\n\n", "Graphics2D g2d = (Graphics2D) c;\ng2d.drawLine(a, b, c, d, e);\n", "javax.swing.text.WrappedPlainView", "drawLine", "int", "int", "java.awt.Graphics", "int", "int"), anyOf(contains("drawLine")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawSelectedText(a, b, c, d, e);\n\n", "Rectangle rect = this.modelToView(startOffset);\nif (rect != null) {\n    JTextComponent component = (JTextComponent) getContainer();\n    Highlighter highlighter = component.getHighlighter();\n    highlighter.paintLayeredHighlights(g, startOffset, endOffset,\n            rect, component, this);\n}\n", "javax.swing.text.WrappedPlainView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawUnselectedText(a, b, c, d, e);\n\n", "Graphics2D g2d = (Graphics2D) a;\ng2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);\ng2d.drawString(getText(b, c), b, d);\n", "javax.swing.text.WrappedPlainView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}