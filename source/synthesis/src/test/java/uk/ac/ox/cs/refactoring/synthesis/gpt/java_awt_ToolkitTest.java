
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
assertThat (synthesiseGPT ("getFontList" , "this.getFontList();" , "\nGraphics g = this.getGraphics();\nFontMetrics fm = g.getFontMetrics();\n// Use fm.getLineMetrics(...) as needed\n" , "java.awt.Toolkit" , "getFontList") , anyOf (contains ("getAvailableFontFamilyNames"))) ;
  }

  @Test
  void getFontMetrics() throws Exception {
assertThat (synthesiseGPT ("getFontMetrics" , "this.getFontMetrics(param0);" , "\nthis.getFontMetrics(param0)\n;" , "java.awt.Toolkit" , "getFontMetrics" , "java.awt.Font") , anyOf (contains ("Font") , contains ("getLineMetrics"))) ;
  }

  @Test
  void getMenuShortcutKeyMask() throws Exception {
assertThat (synthesiseGPT ("getMenuShortcutKeyMask" , "this.getMenuShortcutKeyMask();" , "\njava.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()\n;" , "java.awt.Toolkit" , "getMenuShortcutKeyMask") , anyOf (contains ("getMenuShortcutKeyMaskEx"))) ;
  }
}
