
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
assertThat (synthesiseGPT ("this.getFontList();" , "\nToolkit.getDefaultToolkit().getFontList()\n;" , "java.awt.Toolkit" , "getFontList") , anyOf (contains ("getAvailableFontFamilyNames"))) ;
  }

  @Test
  void getFontMetrics() throws Exception {
assertThat (synthesiseGPT ("this.getFontMetrics(param0);" , "\nToolkit.getDefaultToolkit().getFontMetrics(param0)\n;" , "java.awt.Toolkit" , "getFontMetrics" , "java.awt.Font") , anyOf (contains ("Font") , contains ("getLineMetrics"))) ;
  }

  @Test
  void getMenuShortcutKeyMask() throws Exception {
assertThat (synthesiseGPT ("this.getMenuShortcutKeyMask();" , "\njava.awt.event.KeyEvent.VK_CONTROL // or VK_META for Mac\n;" , "java.awt.Toolkit" , "getMenuShortcutKeyMask") , anyOf (contains ("getMenuShortcutKeyMaskEx"))) ;
  }
}
