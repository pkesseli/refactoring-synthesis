
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_multi_MultiTextUITest {
  @Test
  void modelToView1() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1);" , "param0.getUI().modelToView(param0, param1);" , "javax.swing.plaf.multi.MultiTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1, param2);" , "param0.getUI().modelToView(param0, param1, param2);" , "javax.swing.plaf.multi.MultiTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1);" , "" , "javax.swing.plaf.multi.MultiTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , Matchers . anything ()) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1, param2);" , "\nUtilities.drawTabbedText(param0, param1.x, param1.y, param2, param3, param4);\n;" , "javax.swing.plaf.multi.MultiTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "Position.Bias[]") , Matchers . anything ()) ;
  }
}
