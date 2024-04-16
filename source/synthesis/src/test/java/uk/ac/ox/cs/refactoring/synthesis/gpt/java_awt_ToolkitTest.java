
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ToolkitTest {
  @Test
  void getFontList() throws Exception {
assertThat (synthesiseNeural ("getFontList" , "this.getFontList();" , "\njava.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();\n" , "java.awt.Toolkit" , "getFontList") , anyOf (contains ("getAvailableFontFamilyNames"))) ;
  }

  @Test
  void getFontMetrics() throws Exception {
assertThat (synthesiseNeural ("getFontMetrics" , "this.getFontMetrics(param0);" , "\nthis.getFont(param0).getLineMetrics(\"\", this.getFontRenderContext());\n" , "java.awt.Toolkit" , "getFontMetrics" , "java.awt.Font") , anyOf (contains ("Font") , contains ("getLineMetrics"))) ;
  }

  @Test
  void getMenuShortcutKeyMask() throws Exception {
assertThat (synthesiseNeural ("getMenuShortcutKeyMask" , "this.getMenuShortcutKeyMask();" , "\nthis.getMenuShortcutKeyMaskEx();\n" , "java.awt.Toolkit" , "getMenuShortcutKeyMask") , anyOf (contains ("getMenuShortcutKeyMaskEx"))) ;
  }
}
