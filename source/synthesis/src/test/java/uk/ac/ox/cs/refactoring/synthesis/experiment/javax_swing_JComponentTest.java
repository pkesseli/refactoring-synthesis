package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JComponentTest {
  @Test
  void disable() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "disable"), anyOf(contains("setEnabled")));
  }

  @Test
  void enable() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "enable"), anyOf(contains("setEnabled")));
  }

  @Test
  void getNextFocusableComponent() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "getNextFocusableComponent"), anyOf(contains("FocusTraversalPolicy")));
  }

  // @Test
  // void hide() throws Exception {
  //   assertThat(synthesiseAlias("javax.swing.JComponent", "hide"));
  // }

  @Test
  void isManagingFocus() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "isManagingFocus"), anyOf(contains("setFocusCycleRoot"), contains("setFocusTraversalKeys")));
  }

  @Test
  void requestDefaultFocus() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "requestDefaultFocus"), anyOf(contains("requestFocus")));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "reshape", "int", "int", "int", "int"), anyOf(contains("setBounds")));
  }

  @Test
  void setNextFocusableComponent() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JComponent", "setNextFocusableComponent", "java.awt.Component"), anyOf(contains("FocusTraversalPolicy")));
  }
}