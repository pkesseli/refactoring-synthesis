
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_PasswordViewTest {
  @Test
  void drawEchoCharacter() throws Exception {
    assertThat(synthesiseGPT("this.drawEchoCharacter(a, b, c, d);\n\n", "Graphics2D g2d = (Graphics2D) a;\nFontMetrics fm = g2d.getFontMetrics();\nint charWidth = fm.charWidth(d);\nint x = b + (c - charWidth) / 2;\nint y = fm.getAscent() + b + 1;\ng2d.drawString(String.valueOf(d), x, y);\n", "javax.swing.text.PasswordView", "drawEchoCharacter", "java.awt.Graphics", "int", "int", "char"), anyOf(contains("drawEchoCharacter")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawSelectedText(a, b, c, d, e);\n\n", "Rectangle rect = this.modelToView(start, Position.Bias.Forward);\nRectangle rect2 = this.modelToView(end, Position.Bias.Forward);\nif (rect != null && rect2 != null) {\n    Rectangle r = rect.union(rect2);\n    a.fillRect(r.x, r.y, r.width, r.height);\n}\n", "javax.swing.text.PasswordView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawUnselectedText(a, b, c, d, e);\n\n", "Graphics g = a.create();\ntry {\n    this.getGlyphPainter().paint(this, g, b, c, d, e);\n} finally {\n    g.dispose();\n}\n", "javax.swing.text.PasswordView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}