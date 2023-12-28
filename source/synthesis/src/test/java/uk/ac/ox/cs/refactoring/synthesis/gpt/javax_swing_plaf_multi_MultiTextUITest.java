
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_multi_MultiTextUITest {
  @Test
  void modelToView1() throws Exception {
assertThat (synthesiseGPT ("modelToView1" , "this.modelToView(param0, param1);" , "\nfinal Position.Bias[] bias = new Position.Bias[1];\nfinal Rectangle rectangle = this.modelToView2D(param0, param1, bias);\n" , "javax.swing.plaf.multi.MultiTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("modelToView2" , "this.modelToView(param0, param1, param2);" , "\nfinal TextUI textUI = this.getUI();\nfinal Rectangle2D rectangle2D = textUI.modelToView2D(param0, param1, param2);\nfinal Rectangle rectangle = new Rectangle((int) rectangle2D.getX(), (int) rectangle2D.getY(), (int) rectangle2D.getWidth(), (int) rectangle2D.getHeight());\nrectangle.translate(this.getInsets().left, this.getInsets().top);\nrectangle;\n" , "javax.swing.plaf.multi.MultiTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Disabled("No replacement")
  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("viewToModel1" , "this.viewToModel(param0, param1);" , "\nfinal JTextComponent textComponent = (JTextComponent) this.getComponent(0);\nfinal Point componentPoint = SwingUtilities.convertPoint(this, param1, textComponent);\ntextComponent.viewToModel(componentPoint);\n" , "javax.swing.plaf.multi.MultiTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("viewToModel2" , "this.viewToModel(param0, param1, param2);" , "\nfinal JTextComponent textComponent = (JTextComponent) this.getComponent(0);\nfinal Point convertedPoint = SwingUtilities.convertPoint(this, param1, textComponent);\nfinal int offset = textComponent.viewToModel(convertedPoint);\n" , "javax.swing.plaf.multi.MultiTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "javax.swing.text.Position$Bias[]") , Matchers . anything ()) ;
  }
}
