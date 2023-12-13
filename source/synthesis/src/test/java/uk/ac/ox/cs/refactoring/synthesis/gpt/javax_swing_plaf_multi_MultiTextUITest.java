
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
assertThat (synthesiseGPT ("modelToView1" , "this.modelToView(param0, param1);" , "\ntry {\n    Rectangle2D viewRect = param0.getUI().modelToView(param0, param1);\n    // Use viewRect as needed, for example:\n    // int x = (int) viewRect.getX();\n    // int y = (int) viewRect.getY();\n} catch (BadLocationException e) {\n    // Handle exception\n}\n" , "javax.swing.plaf.multi.MultiTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("modelToView2" , "this.modelToView(param0, param1, param2);" , "\n// Assuming 'param0' is a JTextComponent, 'param1' is an integer position in the document,\n// and 'param2' is a Position.Bias (forward or backward).\nRectangle2D result = null;\ntry {\n    result = param0.getUI().modelToView(param0, param1, param2);\n} catch (BadLocationException e) {\n    // Handle exception\n}\n" , "javax.swing.plaf.multi.MultiTextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("viewToModel1" , "this.viewToModel(param0, param1);" , "\nparam0.getUI().viewToModel(param0, param1)\n;" , "javax.swing.plaf.multi.MultiTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , Matchers . anything ()) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("viewToModel2" , "this.viewToModel(param0, param1, param2);" , "\nTextUI textUI = param0.getUI();\nint modelPosition = textUI.viewToModel(param0, param1, param2);\n" , "javax.swing.plaf.multi.MultiTextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "javax.swing.text.Position$Bias[]") , Matchers . anything ()) ;
  }
}
