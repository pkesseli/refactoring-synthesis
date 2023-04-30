
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicSplitPaneUITest {
  @Test
  void createKeyboardDownRightListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardDownRightListener();\n\n", "this.createDownRightListener();\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardDownRightListener"), Matchers.anything());
  }

  @Test
  void createKeyboardEndListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardEndListener();\n\n", "this.getHandler().createKeyboardEndHandler();\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardEndListener"), Matchers.anything());
  }

  @Test
  void createKeyboardHomeListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardHomeListener();\n\n", "this.getHandler().getHomeHandler().run(); \n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardHomeListener"), Matchers.anything());
  }

  @Test
  void createKeyboardResizeToggleListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardResizeToggleListener();\n\n", "this.createToggleKeyBinding(\n    KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK),\n    \"focusOutForward\",\n    \"negativeIncrement\",\n    \"toggleFocus\"\n);\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardResizeToggleListener"), Matchers.anything());
  }

  @Test
  void createKeyboardUpLeftListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardUpLeftListener();\n\n", "this.getHandler().keyPressed(new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED));\nthis.getHandler().keyPressed(new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardUpLeftListener"), Matchers.anything());
  }

  @Test
  void getDividerBorderSize() throws Exception {
    assertThat(synthesiseGPT("int borderSize = this.getDividerBorderSize();\n\n", "Border border = this.getDivider().getBorder();\nint borderSize = (border != null) ? border.getBorderInsets(this.getDivider()).left : 0;\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "getDividerBorderSize"), Matchers.anything());
  }
}