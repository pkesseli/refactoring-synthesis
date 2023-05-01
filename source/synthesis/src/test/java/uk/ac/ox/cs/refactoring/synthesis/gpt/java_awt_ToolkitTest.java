
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ToolkitTest {
  @Test
  void getFontList() throws Exception {
    assertThat(synthesiseGPT("String[] fontList = Toolkit.getDefaultToolkit().getFontList();\n\n", "String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();\n", "java.awt.Toolkit", "getFontList"), anyOf(contains("getAvailableFontFamilyNames")));
  }

  @Test
  void getFontMetrics() throws Exception {
    assertThat(synthesiseGPT("Font font = new Font(\"Arial\", Font.PLAIN, 12);\nint fontWidth = this.getFontMetrics(font).stringWidth(\"Hello World\");\n\n", "Font font = new Font(\"Arial\", Font.PLAIN, 12);\nFontRenderContext frc = new FontRenderContext(null, true, true);\nint fontWidth = (int) font.getLineMetrics(\"Hello World\", frc).getBounds().getWidth();\n", "java.awt.Toolkit", "getFontMetrics", "java.awt.Font"), anyOf(contains("Font"), contains("getLineMetrics")));
  }

  @Test
  void getMenuShortcutKeyMask() throws Exception {
    assertThat(synthesiseGPT("int shortcutKeyMask = this.getMenuShortcutKeyMask();\n\n", "int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();\n", "java.awt.Toolkit", "getMenuShortcutKeyMask"), anyOf(contains("getMenuShortcutKeyMaskEx")));
  }
}