
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
assertThat (synthesiseGPT ("createKeyboardDownRightListener" , "this.createKeyboardDownRightListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardDownRightListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardEndListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardEndListener" , "this.createKeyboardEndListener();" , "\nJSplitPane splitPane = new JSplitPane();\n\n// Define the action to be taken when the key is pressed\nAction moveDividerToEnd = new AbstractAction() {\n    @Override\n    public void actionPerformed(ActionEvent e) {\n        // Your logic to move the divider to the end\n        splitPane.setDividerLocation(splitPane.getMaximumDividerLocation());\n    }\n};\n\n// Bind the action to a key stroke\nKeyStroke endKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_END, 0);\nInputMap inputMap = splitPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);\nActionMap actionMap = splitPane.getActionMap();\n\ninputMap.put(endKeyStroke, \"moveDividerToEnd\");\nactionMap.put(\"moveDividerToEnd\", moveDividerToEnd);\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardEndListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardHomeListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardHomeListener" , "this.createKeyboardHomeListener();" , "\nKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardHomeListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardResizeToggleListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardResizeToggleListener" , "this.createKeyboardResizeToggleListener();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardResizeToggleListener") , Matchers . anything ()) ;
  }

  @Test
  void createKeyboardUpLeftListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardUpLeftListener" , "this.createKeyboardUpLeftListener();" , "\nActionMap actionMap = this.getActionMap();\nif (actionMap != null) {\n    Action action = actionMap.get(\"negativeIncrement\");\n    if (action != null) {\n        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));\n    }\n}\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardUpLeftListener") , Matchers . anything ()) ;
  }

  @Test
  void getDividerBorderSize() throws Exception {
assertThat (synthesiseGPT ("getDividerBorderSize" , "this.getDividerBorderSize();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "getDividerBorderSize") , Matchers . anything ()) ;
  }
}
