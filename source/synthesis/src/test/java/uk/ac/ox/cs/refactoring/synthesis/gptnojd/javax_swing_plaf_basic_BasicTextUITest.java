
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicTextUITest {
  @Test
  void modelToView1() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = null;\ntry {\n    rect = this.modelToView(a, b);\n} catch (BadLocationException e) {\n    e.printStackTrace();\n}\n\n", "Rectangle rect = a.modelToView(b);\n", "javax.swing.plaf.basic.BasicTextUI", "modelToView", "javax.swing.text.JTextComponent", "int"), anyOf(contains("modelToView2D")));
  }

  @Test
  void modelToView2() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = this.modelToView(a, b, c);\n\n", "Rectangle rect = a.modelToView(b, c);\n", "javax.swing.plaf.basic.BasicTextUI", "modelToView", "javax.swing.text.JTextComponent", "int", "javax.swing.text.Position$Bias"), anyOf(contains("modelToView2D")));
  }

  @Test
  void viewToModel1() throws Exception {
    assertThat(synthesiseGPT("int caretPosition = this.viewToModel(a, b);\n\n", "int caretPosition = a.viewToModel2D(b);\n", "javax.swing.plaf.basic.BasicTextUI", "viewToModel", "javax.swing.text.JTextComponent", "java.awt.Point"), anyOf(contains("viewToModel2D")));
  }

  @Test
  void viewToModel2() throws Exception {
    assertThat(synthesiseGPT("int pos = this.viewToModel(a, b, c);\n\n", "int pos = a.viewToModel2D(b, c);\n", "javax.swing.plaf.basic.BasicTextUI", "viewToModel", "javax.swing.text.JTextComponent", "java.awt.Point", "Position.Bias[]"), anyOf(contains("viewToModel2D")));
  }
}