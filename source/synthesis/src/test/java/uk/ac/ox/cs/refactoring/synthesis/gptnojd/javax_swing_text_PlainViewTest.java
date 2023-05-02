
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_PlainViewTest {
  @Test
  void drawLine() throws Exception {
    assertThat(synthesiseGPT("this.drawLine(a, b, c, d);\n\n", "Graphics g = getGraphics();\ng.drawLine(a, b, c, d);\n", "javax.swing.text.PlainView", "drawLine", "int", "java.awt.Graphics", "int", "int"), anyOf(contains("drawLine")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawSelectedText(a, b, c, d, e);\n\n", "Rectangle rect = this.modelToView(startOffset);\nif (rect != null) {\n    JTextComponent textComponent = (JTextComponent) getContainer();\n    Highlighter highlighter = textComponent.getHighlighter();\n    highlighter.addHighlight(startOffset, endOffset, DefaultHighlighter.DefaultPainter);\n}\n", "javax.swing.text.PlainView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawUnselectedText(a, b, c, d, e);\n\n", "Graphics2D g2d = (Graphics2D) a;\ng2d.setPaint(getForeground());\nDocument doc = getDocument();\nSegment segment = new Segment();\ntry {\n    doc.getText(b, c - b, segment);\n} catch (BadLocationException ex) {\n    ex.printStackTrace();\n}\nFontMetrics fm = g2d.getFontMetrics();\nint x = d;\nint y = e + fm.getAscent();\ng2d.drawString(segment.toString(), x, y);\n", "javax.swing.text.PlainView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}