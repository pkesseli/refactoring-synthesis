
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
    assertThat(synthesiseGPT("this.disable();\n\n", "this.setEnabled(false);\n", "javax.swing.JComponent", "disable"), anyOf(contains("setEnabled")));
  }

  @Test
  void enable() throws Exception {
    assertThat(synthesiseGPT("this.enable();\n\n", "this.setEnabled(true);\n", "javax.swing.JComponent", "enable"), anyOf(contains("setEnabled")));
  }

  @Test
  void getNextFocusableComponent() throws Exception {
    assertThat(synthesiseGPT("Component nextFocusableComponent = this.getNextFocusableComponent();\n\n", "FocusTraversalPolicy policy = this.getFocusTraversalPolicy();\nComponent nextFocusableComponent = policy.getComponentAfter(this, null);\n", "javax.swing.JComponent", "getNextFocusableComponent"), anyOf(contains("FocusTraversalPolicy")));
  }

  @Test
  void hide() throws Exception {
    assertThat(synthesiseGPT("this.hide();\n\n", "this.setVisible(false);\n", "javax.swing.JComponent", "hide"), Matchers.anything());
  }

  @Test
  void isManagingFocus() throws Exception {
    assertThat(synthesiseGPT("if (this.isManagingFocus()) {\n    // do something\n}\n", "if (this.isFocusCycleRoot()) {\n    // do something\n}\n", "javax.swing.JComponent", "isManagingFocus"), anyOf(contains("setFocusCycleRoot"), contains("setFocusTraversalKeys")));
  }

  @Test
  void requestDefaultFocus() throws Exception {
    assertThat(synthesiseGPT("this.requestDefaultFocus();\n", "this.getFocusTraversalPolicy().getDefaultComponent(this).requestFocus();\n", "javax.swing.JComponent", "requestDefaultFocus"), anyOf(contains("requestFocus")));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseGPT("this.reshape(a, b, c, d);\n", "this.setBounds(a, b, c, d);\n", "javax.swing.JComponent", "reshape", "int", "int", "int", "int"), anyOf(contains("setBounds")));
  }

  @Test
  void setNextFocusableComponent() throws Exception {
    assertThat(synthesiseGPT("this.setNextFocusableComponent(a);\n", "this.setFocusTraversalPolicyProvider(true);\nthis.setFocusTraversalPolicy(new FocusTraversalPolicy() {\n    @Override\n    public Component getComponentAfter(Container container, Component component) {\n        return a;\n    }\n\n    @Override\n    public Component getComponentBefore(Container container, Component component) {\n        return null;\n    }\n\n    @Override\n    public Component getDefaultComponent(Container container) {\n        return a;\n    }\n\n    @Override\n    public Component getLastComponent(Container container) {\n        return a;\n    }\n\n    @Override\n    public Component getFirstComponent(Container container) {\n        return a;\n    }\n});\n", "javax.swing.JComponent", "setNextFocusableComponent", "java.awt.Component"), anyOf(contains("FocusTraversalPolicy")));
  }
}