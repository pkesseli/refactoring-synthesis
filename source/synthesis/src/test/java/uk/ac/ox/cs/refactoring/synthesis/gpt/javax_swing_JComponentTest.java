
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JComponentTest {
  @Test
  void disable() throws Exception {
assertThat (synthesiseGPT ("this.disable();" , "\nthis.setEnabled(false);\n;" , "javax.swing.JComponent" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable() throws Exception {
assertThat (synthesiseGPT ("this.enable();" , "\nthis.setEnabled(true);\n;" , "javax.swing.JComponent" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void getNextFocusableComponent() throws Exception {
assertThat (synthesiseGPT ("this.getNextFocusableComponent();" , "\nthis.transferFocus();\n;" , "javax.swing.JComponent" , "getNextFocusableComponent") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }

  @Test
  void hide() throws Exception {
assertThat (synthesiseGPT ("this.hide();" , "\nthis.setVisible(false);\n;" , "javax.swing.JComponent" , "hide") , Matchers . anything ()) ;
  }

  @Test
  void isManagingFocus() throws Exception {
assertThat (synthesiseGPT ("this.isManagingFocus();" , "\nthis.isFocusable()\n;" , "javax.swing.JComponent" , "isManagingFocus") , anyOf (contains ("setFocusCycleRoot") , contains ("setFocusTraversalKeys"))) ;
  }

  @Test
  void requestDefaultFocus() throws Exception {
assertThat (synthesiseGPT ("this.requestDefaultFocus();" , "\nthis.requestFocusInWindow();\n;" , "javax.swing.JComponent" , "requestDefaultFocus") , anyOf (contains ("requestFocus"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("this.reshape(param0, param1, param2, param3);" , "\nthis.setBounds(param0, param1, param2, param3);\n;" , "javax.swing.JComponent" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void setNextFocusableComponent() throws Exception {
assertThat (synthesiseGPT ("this.setNextFocusableComponent(param0);" , "\n// Assuming 'this' is a JComponent and 'param0' is another JComponent\nthis.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {\n    public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {\n        return param0;\n    }\n});\nthis.setFocusTraversalPolicyProvider(true);\n```\n\nHowever, if you simply want to set the focus traversal keys to move the focus to the next component, you can use the `setFocusTraversalKeys` method:\n\n```java\n// Assuming 'this' is a JComponent and 'param0' is another JComponent\nSet<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(\n    this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS)\n);\nforwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));\nthis.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);\n\n// You would then need to add a FocusListener to 'this' to manually transfer focus to 'param0'\nthis.addFocusListener(new FocusAdapter() {\n    public void focusGained(FocusEvent e) {\n        if (e.getOppositeComponent() != null) {\n            param0.requestFocus();\n        }\n    }\n});\n;" , "javax.swing.JComponent" , "setNextFocusableComponent" , "java.awt.Component") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }
}
