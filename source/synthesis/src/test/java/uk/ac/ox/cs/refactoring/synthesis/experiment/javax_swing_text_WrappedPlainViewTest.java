
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_text_WrappedPlainViewTest {
  @Test
  void drawLine() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.WrappedPlainView", "drawLine", "int", "int", "java.awt.Graphics", "int", "int"), anyOf(contains("drawLine")));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.WrappedPlainView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawSelectedText")));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.WrappedPlainView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), anyOf(contains("drawUnselectedText")));
  }
}