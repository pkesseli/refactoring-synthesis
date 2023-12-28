
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_TextUITest {
  @Test
  void getToolTipText() throws Exception {
assertThat (synthesiseGPT ("getToolTipText" , "this.getToolTipText(param0, param1);" , "\nfinal Point2D point2D = new Point2D.Float(param1.x, param1.y);\nthis.getToolTipText2D(param0, point2D);\n" , "javax.swing.plaf.TextUI" , "getToolTipText" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("getToolTipText2D"))) ;
  }

  @Test
  void modelToView1() throws Exception {
assertThat (synthesiseGPT ("modelToView1" , "this.modelToView(param0, param1);" , "\nfinal Position.Bias[] bias = new Position.Bias[1];\nfinal Rectangle rectangle = this.modelToView2D(param0, param1, bias);\n" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("modelToView2" , "this.modelToView(param0, param1, param2);" , "\nfinal TextUI textUI = this.getUI();\nfinal Rectangle2D rectangle2D = textUI.modelToView2D(this, param1, param2);\nfinal Rectangle rectangle = new Rectangle((int) rectangle2D.getX(), (int) rectangle2D.getY(), (int) rectangle2D.getWidth(), (int) rectangle2D.getHeight());\nrectangle.translate(this.getInsets().left, this.getInsets().top);\nrectangle;\n" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("viewToModel1" , "this.viewToModel(param0, param1);" , "\nfinal Point2D point2D = new Point2D.Float(param1.x, param1.y);\nfinal Position.Bias[] bias = new Position.Bias[1];\nthis.viewToModel2D(param0, point2D, bias);\n" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("viewToModel2D"))) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("viewToModel2" , "this.viewToModel(param0, param1, param2);" , "\nfinal Point2D point2D = new Point2D.Float(param1.x, param1.y);\nthis.viewToModel2D(param0, point2D, param2);\n" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "javax.swing.text.Position$Bias[]") , anyOf (contains ("viewToModel2D"))) ;
  }
}
