
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ToolkitTest {
  @Test
  void getFontList() throws Exception {
    assertThat(synthesiseGPT("String[] fonts = Toolkit.getDefaultToolkit().getFontList(null);\n\n", "String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();\n", "java.awt.Toolkit", "getFontList"), anyOf(contains("getAvailableFontFamilyNames")));
  }

  @Test
  void getFontMetrics() throws Exception {
    assertThat(synthesiseGPT("Font font = new Font(\"Arial\", Font.PLAIN, 12);\nint width = this.getFontMetrics(font).stringWidth(\"Hello World\");\n\n", "Font font = new Font(\"Arial\", Font.PLAIN, 12);\nFontMetrics fontMetrics = this.getFontMetrics(font);\nint width = fontMetrics.stringWidth(\"Hello World\");\n", "java.awt.Toolkit", "getFontMetrics", "java.awt.Font"), anyOf(contains("Font"), contains("getLineMetrics")));
  }

  @Test
  void getMenuShortcutKeyMask() throws Exception {
    assertThat(synthesiseGPT("int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();\n\n", "int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();\n", "java.awt.Toolkit", "getMenuShortcutKeyMask"), anyOf(contains("getMenuShortcutKeyMaskEx")));
  }
}