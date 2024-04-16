
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_JComponentTest {
  @Test
  void disable() throws Exception {
assertThat (synthesiseNeural ("disable" , "this.disable();" , "\nthis.setEnabled(false);\n" , "javax.swing.JComponent" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable() throws Exception {
assertThat (synthesiseNeural ("enable" , "this.enable();" , "\nthis.setEnabled(true);\n" , "javax.swing.JComponent" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void getNextFocusableComponent() throws Exception {
assertThat (synthesiseNeural ("getNextFocusableComponent" , "this.getNextFocusableComponent();" , "\nthis.getFocusTraversalPolicy().getComponentAfter(this);\n" , "javax.swing.JComponent" , "getNextFocusableComponent") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }

  @Disabled("No replacement")
  @Test
  void hide() throws Exception {
assertThat (synthesiseNeural ("hide" , "this.hide();" , "\nthis.setVisible(false);\n" , "javax.swing.JComponent" , "hide") , Matchers . anything ()) ;
  }

  @Test
  void isManagingFocus() throws Exception {
assertThat (synthesiseNeural ("isManagingFocus" , "this.isManagingFocus();" , "\nthis.setFocusTraversalKeysEnabled(true);\nthis.setFocusCycleRoot(true);\n" , "javax.swing.JComponent" , "isManagingFocus") , anyOf (contains ("setFocusCycleRoot") , contains ("setFocusTraversalKeys"))) ;
  }

  @Test
  void requestDefaultFocus() throws Exception {
assertThat (synthesiseNeural ("requestDefaultFocus" , "this.requestDefaultFocus();" , "\nFocusTraversalPolicy.getDefaulComponent(this).requestFocus();\n" , "javax.swing.JComponent" , "requestDefaultFocus") , anyOf (contains ("requestFocus"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseNeural ("reshape" , "this.reshape(param0, param1, param2, param3);" , "\nthis.setBounds(param0, param1, param2, param3);\n" , "javax.swing.JComponent" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void setNextFocusableComponent() throws Exception {
assertThat (synthesiseNeural ("setNextFocusableComponent" , "this.setNextFocusableComponent(param0);" , "\nthis.setFocusTraversalPolicyProvider(true);\nthis.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {\n    public Component getDefaultComponent(Container focusCycleRoot) {\n        return param0;\n    }\n});\n" , "javax.swing.JComponent" , "setNextFocusableComponent" , "java.awt.Component") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }
}
