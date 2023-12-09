
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
assertThat (synthesiseGPT ("this.getToolTipText(param0, param1);" , "\nMouseEvent me = new MouseEvent(param0, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, param1.x, param1.y, 0, false);\nString toolTipText = param0.getToolTipText(me);\n" , "javax.swing.plaf.TextUI" , "getToolTipText" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("getToolTipText2D"))) ;
  }

  @Test
  void modelToView1() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1);" , "\nUtilities.getPositionAbove(param0, param1, 0.5f)\n;" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1, param2);" , "\nUtilities.getPositionBelow(param0, param1, 1.0f)\n;" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1);" , "\nint offset = Utilities.getTabbedTextOffset(\n    new Segment(param0.getText().toCharArray(), 0, param0.getDocument().getLength()),\n    param0.getFontMetrics(param0.getFont()),\n    param1.x,\n    param1.x,\n    this,\n    this.viewToModel(param0, new Point(0, param1.y)),\n    false\n);\n" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("viewToModel2D"))) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1, param2);" , "\nint offset = param0.viewToModel(param1);\n" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "Position.Bias[]") , anyOf (contains ("viewToModel2D"))) ;
  }
}
