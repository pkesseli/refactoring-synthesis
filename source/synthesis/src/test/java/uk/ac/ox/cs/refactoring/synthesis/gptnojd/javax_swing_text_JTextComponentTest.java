
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_JTextComponentTest {
  @Test
  void modelToView() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = this.modelToView(a);\n\n", "Rectangle rect = null;\ntry {\n    rect = this.modelToView(a);\n} catch (BadLocationException e) {\n    e.printStackTrace();\n}\n", "javax.swing.text.JTextComponent", "modelToView", "int"), anyOf(contains("modelToView2D")));
  }

  @Test
  void viewToModel() throws Exception {
    assertThat(synthesiseGPT("int offset = this.viewToModel(a);\n\n", "int offset = this.getUI().viewToModel(this, a);\n", "javax.swing.text.JTextComponent", "viewToModel", "java.awt.Point"), anyOf(contains("viewToModel2D")));
  }
}