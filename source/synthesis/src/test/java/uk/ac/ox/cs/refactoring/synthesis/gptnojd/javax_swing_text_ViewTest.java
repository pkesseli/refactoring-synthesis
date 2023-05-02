
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_ViewTest {
  @Test
  void modelToView() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = this.modelToView(a, b);\n\n", "Rectangle rect = null;\ntry {\n    rect = this.modelToView(a);\n} catch (BadLocationException e) {\n    e.printStackTrace();\n}\n", "javax.swing.text.View", "modelToView", "int", "java.awt.Shape"), Matchers.anything());
  }

  @Test
  void viewToModel() throws Exception {
    assertThat(synthesiseGPT("int pos = this.viewToModel(a, b, c);\n\n", "int pos = this.viewToModel((int) a, (int) b, c);\n", "javax.swing.text.View", "viewToModel", "float", "float", "java.awt.Shape"), Matchers.anything());
  }
}