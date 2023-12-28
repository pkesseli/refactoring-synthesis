
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicSplitPaneUITest {
  @Disabled("No replacement")
  @Test
  void createKeyboardDownRightListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardDownRightListener" , "this.createKeyboardDownRightListener();" , "\nfinal BasicSplitPaneUI.KeyboardDownRightHandler handler = new BasicSplitPaneUI.KeyboardDownRightHandler();\nhandler.actionPerformed(null);\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardDownRightListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardEndListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardEndListener" , "this.createKeyboardEndListener();" , "\nfinal BasicSplitPaneUI.KeyboardEndHandler keyboardEndHandler = new BasicSplitPaneUI.KeyboardEndHandler();\nkeyboardEndHandler.actionPerformed(null);\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardEndListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardHomeListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardHomeListener" , "this.createKeyboardHomeListener();" , "\nfinal KeyStroke homeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0);\nfinal Action homeAction = new AbstractAction() {\n    @Override\n    public void actionPerformed(final ActionEvent e) {\n        final BasicSplitPaneUI ui = (BasicSplitPaneUI) e.getSource();\n        final JSplitPane splitPane = ui.getSplitPane();\n        final Component leftComponent = splitPane.getLeftComponent();\n        if (leftComponent != null) {\n            final Rectangle bounds = leftComponent.getBounds();\n            bounds.x = 0;\n            bounds.y = 0;\n            bounds.width = splitPane.getDividerLocation();\n            bounds.height = splitPane.getHeight();\n            splitPane.setDividerLocation(0);\n            leftComponent.scrollRectToVisible(bounds);\n        }\n    }\n};\nfinal InputMap inputMap = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);\ninputMap.put(homeKeyStroke, \"home\");\nthis.getActionMap().put(\"home\", homeAction);\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardHomeListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardResizeToggleListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardResizeToggleListener" , "this.createKeyboardResizeToggleListener();" , "\n// Create a new instance of the KeyboardResizeToggleListener class\nfinal KeyboardResizeToggleListener listener = new KeyboardResizeToggleListener() {\n    @Override\n    public void keyPressed(final KeyEvent e) {\n        // Handle key press event\n    }\n\n    @Override\n    public void keyReleased(final KeyEvent e) {\n        // Handle key release event\n    }\n};\n\n// Add the listener to the split pane UI\nthis.addKeyListener(listener);\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardResizeToggleListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardUpLeftListener() throws Exception {
assertThat (synthesiseGPT ("createKeyboardUpLeftListener" , "this.createKeyboardUpLeftListener();" , "\nfinal ActionListener[] keyboardListeners = this.getKeyboardListeners();\nfor (final ActionListener listener : keyboardListeners) {\n    if (listener instanceof BasicSplitPaneUI.KeyboardUpLeftHandler) {\n        return (BasicSplitPaneUI.KeyboardUpLeftHandler) listener;\n    }\n}\nreturn null;\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardUpLeftListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getDividerBorderSize() throws Exception {
assertThat (synthesiseGPT ("getDividerBorderSize" , "this.getDividerBorderSize();" , "\nfinal BasicSplitPaneDivider divider = this.getDivider();\nfinal Border border = divider.getBorder();\nfinal Insets insets = border.getBorderInsets(divider);\ninsets.left + insets.right + insets.top + insets.bottom;\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "getDividerBorderSize") , Matchers . anything ()) ;
  }
}
