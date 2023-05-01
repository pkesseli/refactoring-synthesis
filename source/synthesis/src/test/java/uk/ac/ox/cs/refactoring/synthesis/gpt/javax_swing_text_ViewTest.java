
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_ViewTest {
  @Test
  void modelToView() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = this.modelToView(a, b);\n\n", "Rectangle rect = this.modelToView(a);\n", "javax.swing.text.View", "modelToView", "int", "java.awt.Shape"), Matchers.anything());
  }

  @Test
  void viewToModel() throws Exception {
    assertThat(synthesiseGPT("int pos = this.viewToModel(a, b, c);\n\n", "Point2D.Float point = new Point2D.Float(a, b);\npos = this.viewToModel(point, c);\n", "javax.swing.text.View", "viewToModel", "float", "float", "java.awt.Shape"), Matchers.anything());
  }
}