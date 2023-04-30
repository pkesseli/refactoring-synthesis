
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicTextUITest {
  @Test
  void modelToView1() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = this.modelToView(a, b);\n\n", "Rectangle2D rect = this.modelToView2D(a, b, Position.Bias.Forward);\n", "javax.swing.plaf.basic.BasicTextUI", "modelToView", "javax.swing.text.JTextComponent", "int"), anyOf(contains("modelToView2D")));
  }

  @Test
  void modelToView2() throws Exception {
    assertThat(synthesiseGPT("Rectangle rect = this.modelToView(a, b, c);\n\n", "Rectangle2D rect = this.modelToView2D(a, b, c);\n", "javax.swing.plaf.basic.BasicTextUI", "modelToView", "javax.swing.text.JTextComponent", "int", "javax.swing.text.Position$Bias"), anyOf(contains("modelToView2D")));
  }

  @Test
  void viewToModel1() throws Exception {
    assertThat(synthesiseGPT("int pos = this.viewToModel(a, b);\n\n", "Position.Bias[] bias = new Position.Bias[1];\nint pos = this.getUI().viewToModel2D(a, null, bias);\n", "javax.swing.plaf.basic.BasicTextUI", "viewToModel", "javax.swing.text.JTextComponent", "java.awt.Point"), anyOf(contains("viewToModel2D")));
  }

  @Test
  void viewToModel2() throws Exception {
    assertThat(synthesiseGPT("int pos = this.viewToModel(a, b, c);\n\n", "int pos = this.getUI().viewToModel2D(a, b, c);\n", "javax.swing.plaf.basic.BasicTextUI", "viewToModel", "javax.swing.text.JTextComponent", "java.awt.Point", "Position.Bias[]"), anyOf(contains("viewToModel2D")));
  }
}