
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicTextUITest {
  @Test
  void modelToView1() throws Exception {
    assertThat(synthesiseAlias("javax.swing.plaf.basic.BasicTextUI", "modelToView", "javax.swing.text.JTextComponent", "int"), anyOf(contains("modelToView2D")));
  }

  @Test
  void modelToView2() throws Exception {
    assertThat(synthesiseAlias("javax.swing.plaf.basic.BasicTextUI", "modelToView", "javax.swing.text.JTextComponent", "int", "Position$Bias"), anyOf(contains("modelToView2D")));
  }

  @Test
  void viewToModel1() throws Exception {
    assertThat(synthesiseAlias("javax.swing.plaf.basic.BasicTextUI", "viewToModel", "javax.swing.text.JTextComponent", "java.awt.Point"), anyOf(contains("viewToModel2D")));
  }

  @Test
  void viewToModel2() throws Exception {
    assertThat(synthesiseAlias("javax.swing.plaf.basic.BasicTextUI", "viewToModel", "javax.swing.text.JTextComponent", "java.awt.Point", "javax.swing.text.Position$Bias[]"), anyOf(contains("viewToModel2D")));
  }
}