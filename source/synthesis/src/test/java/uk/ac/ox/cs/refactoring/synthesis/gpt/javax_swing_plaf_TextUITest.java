
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_TextUITest {
  @Test
  void getToolTipText() throws Exception {
assertThat (synthesiseNeural ("getToolTipText" , "this.getToolTipText(param0, param1);" , "\nthis.getToolTipText2D(param0, param1);\n" , "javax.swing.plaf.TextUI" , "getToolTipText" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("getToolTipText2D"))) ;
  }

  @Test
  void modelToView1() throws Exception {
assertThat (synthesiseNeural ("modelToView1" , "this.modelToView(param0, param1);" , "\nthis.modelToView2D(param0, param1, Position.Bias.Forward);\n" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseNeural ("modelToView2" , "this.modelToView(param0, param1, param2);" , "\nthis.modelToView2D(param0, param1, param2);\n" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseNeural ("viewToModel1" , "this.viewToModel(param0, param1);" , "\nthis.viewToModel2D(param0, param1, null)[0];\n" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("viewToModel2D"))) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseNeural ("viewToModel2" , "this.viewToModel(param0, param1, param2);" , "\nthis.viewToModel2D(param0, param1, param2);\n" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "javax.swing.text.Position$Bias[]") , anyOf (contains ("viewToModel2D"))) ;
  }
}
