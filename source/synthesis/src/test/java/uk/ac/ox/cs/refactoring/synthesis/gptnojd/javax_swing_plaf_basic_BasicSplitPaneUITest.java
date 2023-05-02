
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicSplitPaneUITest {
  @Test
  void createKeyboardDownRightListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardDownRightListener();\n\n", "this.getHandler().getKeyboardDownRightHandler();\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardDownRightListener"), Matchers.anything());
  }

  @Test
  void createKeyboardEndListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardEndListener();\n\n", "this.getKeyboardEndListener();\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardEndListener"), Matchers.anything());
  }

  @Test
  void createKeyboardHomeListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardHomeListener();\n\n", "this.getHandler().getKeyboardHomeHandler().run(); \n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardHomeListener"), Matchers.anything());
  }

  @Test
  void createKeyboardResizeToggleListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardResizeToggleListener();\n\n", "splitPane.setDividerKeyboardAction(new ToggleAction());\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardResizeToggleListener"), Matchers.anything());
  }

  @Test
  void createKeyboardUpLeftListener() throws Exception {
    assertThat(synthesiseGPT("this.createKeyboardUpLeftListener();\n\n", "this.getHandler().getKeyboardUpLeftHandler().run(); \n", "javax.swing.plaf.basic.BasicSplitPaneUI", "createKeyboardUpLeftListener"), Matchers.anything());
  }

  @Test
  void getDividerBorderSize() throws Exception {
    assertThat(synthesiseGPT("int borderSize = this.getDividerBorderSize();\n\n", "int borderSize = ((BasicSplitPaneUI) this.getUI()).getDivider().getBorder().getBorderInsets(this.getDivider()).left;\n", "javax.swing.plaf.basic.BasicSplitPaneUI", "getDividerBorderSize"), Matchers.anything());
  }
}