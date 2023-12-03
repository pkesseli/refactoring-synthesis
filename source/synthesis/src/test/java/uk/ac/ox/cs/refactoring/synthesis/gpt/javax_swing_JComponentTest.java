
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
assertThat (synthesiseGPT ("this.disable();" , "\nthis.setScrollMode(JScrollPane.WHEN_DISABLED)\n;" , "javax.swing.JComponent" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable() throws Exception {
assertThat (synthesiseGPT ("this.enable();" , "" , "javax.swing.JComponent" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void getNextFocusableComponent() throws Exception {
assertThat (synthesiseGPT ("this.getNextFocusableComponent();" , "" , "javax.swing.JComponent" , "getNextFocusableComponent") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }

  @Test
  void hide() throws Exception {
assertThat (synthesiseGPT ("this.hide();" , "\nthis.setVisible(false);\n;" , "javax.swing.JComponent" , "hide") , Matchers . anything ()) ;
  }

  @Test
  void isManagingFocus() throws Exception {
assertThat (synthesiseGPT ("this.isManagingFocus();" , "\nthis.getFocusTraversalPolicy().getFirstComponent() == this\n;" , "javax.swing.JComponent" , "isManagingFocus") , anyOf (contains ("setFocusCycleRoot") , contains ("setFocusTraversalKeys"))) ;
  }

  @Test
  void requestDefaultFocus() throws Exception {
assertThat (synthesiseGPT ("this.requestDefaultFocus();" , "" , "javax.swing.JComponent" , "requestDefaultFocus") , anyOf (contains ("requestFocus"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("this.reshape(param0, param1, param2, param3);" , "" , "javax.swing.JComponent" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void setNextFocusableComponent() throws Exception {
assertThat (synthesiseGPT ("this.setNextFocusableComponent(param0);" , "\nthis.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));\nparam0.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));\n;" , "javax.swing.JComponent" , "setNextFocusableComponent" , "java.awt.Component") , anyOf (contains ("FocusTraversalPolicy"))) ;
  }
}
