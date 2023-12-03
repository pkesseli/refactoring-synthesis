
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
assertThat (synthesiseGPT ("this.createKeyboardDownRightListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardDownRightListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardEndListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardEndListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardEndListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardHomeListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardHomeListener();" , "KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardHomeListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardResizeToggleListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardResizeToggleListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardResizeToggleListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardUpLeftListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardUpLeftListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardUpLeftListener") , Matchers . anything ()) ;
  }

  @Test
  void getDividerBorderSize() throws Exception {
assertThat (synthesiseGPT ("this.getDividerBorderSize();" , "\nthis.getInsets().left // or right, depending on the orientation of the split pane\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "getDividerBorderSize") , Matchers . anything ()) ;
  }
}
