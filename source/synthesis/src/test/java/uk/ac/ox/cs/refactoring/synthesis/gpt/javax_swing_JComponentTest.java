
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_JComponentTest {
  @Test
  void disable() throws Exception {
assertThat (synthesiseGPT ("disable" , "this.disable();" , "\nthis.setEnabled(false);\n" , "javax.swing.JComponent" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable() throws Exception {
assertThat (synthesiseGPT ("enable" , "this.enable();" , "\nthis.setEnabled(true);\n" , "javax.swing.JComponent" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void getNextFocusableComponent() throws Exception {
assertThat (synthesiseGPT ("getNextFocusableComponent" , "this.getNextFocusableComponent();" , "\nfinal FocusTraversalPolicy policy = this.getFocusTraversalPolicy();\npolicy.getComponentAfter(this);\n" , "javax.swing.JComponent" , "getNextFocusableComponent") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }

  @Disabled("No replacement")
  @Test
  void hide() throws Exception {
assertThat (synthesiseGPT ("hide" , "this.hide();" , "\nthis.setVisible(false);\n" , "javax.swing.JComponent" , "hide") , Matchers . anything ()) ;
  }

  @Test
  void isManagingFocus() throws Exception {
assertThat (synthesiseGPT ("isManagingFocus" , "this.isManagingFocus();" , "\nthis.isFocusCycleRoot();\n" , "javax.swing.JComponent" , "isManagingFocus") , anyOf (contains ("setFocusCycleRoot") , contains ("setFocusTraversalKeys"))) ;
  }

  @Test
  void requestDefaultFocus() throws Exception {
assertThat (synthesiseGPT ("requestDefaultFocus" , "this.requestDefaultFocus();" , "\nfinal Container container = this.getParent();\nif (container != null) {\n    final Component defaultComponent = container.getFocusTraversalPolicy().getDefaultComponent(container);\n    if (defaultComponent != null) {\n        defaultComponent.requestFocus();\n    }\n}\n" , "javax.swing.JComponent" , "requestDefaultFocus") , anyOf (contains ("requestFocus"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("reshape" , "this.reshape(param0, param1, param2, param3);" , "\nthis.setBounds(param0, param1, param2, param3);\n" , "javax.swing.JComponent" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void setNextFocusableComponent() throws Exception {
assertThat (synthesiseGPT ("setNextFocusableComponent" , "this.setNextFocusableComponent(param0);" , "\nfinal FocusTraversalPolicy policy = this.getFocusTraversalPolicy();\npolicy.setComponentAfter(this, param0);\nthis.setFocusTraversalPolicy(policy);\n" , "javax.swing.JComponent" , "setNextFocusableComponent" , "java.awt.Component") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }
}
