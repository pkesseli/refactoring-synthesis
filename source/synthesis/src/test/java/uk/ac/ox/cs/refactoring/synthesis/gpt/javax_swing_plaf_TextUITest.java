
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
assertThat (synthesiseGPT ("this.getToolTipText(param0, param1);" , "\nUtilities.getBreakLocation(param0.getDocument().getDefaultRootElement().getElement(0).createSegment(), \n                            param0.getFontMetrics(param0.getFont()), \n                            param1.getX(), \n                            param1.getY(), \n                            param0.getUI().getTabExpander(), \n                            param0.getInsets().left);\n;" , "javax.swing.plaf.TextUI" , "getToolTipText" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("getToolTipText2D"))) ;
  }

  @Test
  void modelToView1() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1);" , "\nUtilities.getPositionAbove(param0, param1, 0f)\n;" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void modelToView2() throws Exception {
assertThat (synthesiseGPT ("this.modelToView(param0, param1, param2);" , "\nUtilities.getPositionBelow(param0, param1, param2.getOffsetBias())\n;" , "javax.swing.plaf.TextUI" , "modelToView" , "javax.swing.text.JTextComponent" , "int" , "javax.swing.text.Position$Bias") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel1() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1);" , "Utilities.getTabbedTextOffset(param0.getDocument().getDefaultRootElement().getElement(0).getDocument().createPosition(param1), param0.getFontMetrics(param0.getFont()), param1.getX(), param1.getY(), param0.getUI().getTabExpander(), param0.getHorizontalAlignment(), param0.getComponentOrientation().isLeftToRight());" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point") , anyOf (contains ("viewToModel2D"))) ;
  }

  @Test
  void viewToModel2() throws Exception {
assertThat (synthesiseGPT ("this.viewToModel(param0, param1, param2);" , "Utilities.getTabbedTextOffset(param0.getDocument().getDefaultRootElement().getElement(0).getDocument().createPosition(0), param0.getFontMetrics(param0.getFont()), param1.getX(), param1.getY(), param0.getUI().getTabExpander(), Position.Bias.Forward, param2[0] == Position.Bias.Backward);" , "javax.swing.plaf.TextUI" , "viewToModel" , "javax.swing.text.JTextComponent" , "java.awt.Point" , "Position.Bias[]") , anyOf (contains ("viewToModel2D"))) ;
  }
}
