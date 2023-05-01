
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
    assertThat(synthesiseGPT("this.drawEchoCharacter(a, b, c, d);\n\n", "Graphics2D g2d = (Graphics2D) a;\nthis.drawEchoCharacter(g2d, (float) b, (float) c, d);\n", "javax.swing.text.PasswordView", "drawEchoCharacter", "java.awt.Graphics", "int", "int", "char"), anyOf(contains("drawEchoCharacter")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawSelectedText(a, b, c, d, e);\n", "((Graphics2D) a).setColor(this.getSelectedTextColor());\n((Graphics2D) a).setBackground(this.getBackground());\n((Graphics2D) a).fill(this.getVisibleEditorRect());\n((Graphics2D) a).setColor(this.getForeground());\n((Graphics2D) a).drawString(this.getSelectedText().toString(), b, c);\n", "javax.swing.text.PasswordView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseGPT("this.drawUnselectedText(a, b, c, d, e);\n", "((Graphics2D) a).setColor(getTextColors().getForeground());\n((Graphics2D) a).setFont(getFont());\n((Graphics2D) a).drawString(getText(c, d), b, d);\n", "javax.swing.text.PasswordView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}