
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_JTextComponentTest {
  @Test
  void modelToView() throws Exception {
assertThat (synthesiseNeural ("modelToView" , "this.modelToView(param0);" , "\nthis.modelToView2D(param0);\n" , "javax.swing.text.JTextComponent" , "modelToView" , "int") , anyOf (contains ("modelToView2D"))) ;
  }

  @Test
  void viewToModel() throws Exception {
assertThat (synthesiseNeural ("viewToModel" , "this.viewToModel(param0);" , "\nthis.viewToModel2D(param0);\n" , "javax.swing.text.JTextComponent" , "viewToModel" , "java.awt.Point") , anyOf (contains ("viewToModel2D"))) ;
  }
}
