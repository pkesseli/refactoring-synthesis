
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
assertThat (synthesiseGPT ("this.createKeyboardEndListener();" , "\nJSplitPane splitPane = ...; // your JSplitPane instance\n\nKeyAdapter endKeyListener = new KeyAdapter() {\n    @Override\n    public void keyPressed(KeyEvent e) {\n        if (e.getKeyCode() == KeyEvent.VK_END) {\n            // Handle the end key press event\n            // You need to define what should happen when the end key is pressed\n        }\n    }\n};\n\nsplitPane.addKeyListener(endKeyListener);\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardEndListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardHomeListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardHomeListener();" , "\nKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardHomeListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardResizeToggleListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardResizeToggleListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardResizeToggleListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardUpLeftListener() throws Exception {
assertThat (synthesiseGPT ("this.createKeyboardUpLeftListener();" , "\nActionMap actionMap = this.getActionMap();\nif (actionMap != null) {\n    Action action = actionMap.get(\"negativeIncrement\");\n    if (action != null) {\n        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));\n    }\n}\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardUpLeftListener") , Matchers . anything ()) ;
  }

  @Test
  void getDividerBorderSize() throws Exception {
assertThat (synthesiseGPT ("this.getDividerBorderSize();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "getDividerBorderSize") , Matchers . anything ()) ;
  }
}
