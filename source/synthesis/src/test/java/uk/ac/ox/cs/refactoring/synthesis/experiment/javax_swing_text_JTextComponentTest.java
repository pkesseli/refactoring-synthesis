package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_text_JTextComponentTest {
  @Test
  void viewToModel() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.JTextComponent", "viewToModel", "java.awt.Point"), contains("viewToModel2D"));
  }
}
