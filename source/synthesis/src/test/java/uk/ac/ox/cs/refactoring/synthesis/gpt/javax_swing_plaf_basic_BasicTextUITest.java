
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
assertThat (synthesiseGPT ("this.modelToView(param0, param1);" , "\nparam0.modelToView2D(param1)\n;" , "javax.swing.plaf.basic.BasicTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1, param2);" , "param0.viewToModel2D(new Point(param1, 0)).getOffset();" , "javax.swing.plaf.basic.BasicTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1);" , "\nparam0.viewToModel(param1)\n;" , "javax.swing.plaf.basic.BasicTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("viewToModel2D"))) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1, param2);" , "param0.viewToModel2D(param1, param2);" , "javax.swing.plaf.basic.BasicTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "Position.Bias[]") , anyOf (contains ("viewToModel2D"))) ;
  }
}
