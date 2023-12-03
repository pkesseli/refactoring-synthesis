
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_metal_MetalComboBoxUITest {
  @Test
  void editablePropertyChanged() throws Exception {
assertThat (synthesiseGPT ("this.editablePropertyChanged(param0);" , "\nthis.getEditor().setItem(param0.getNewValue().toString());\n;" , "javax.swing.plaf.metal.MetalComboBoxUI" , "editablePropertyChanged" , "java.beans.PropertyChangeEvent") , Matchers . anything ()) ;
  }

  @Test
  void removeListeners() throws Exception {
assertThat (synthesiseGPT ("this.removeListeners();" , "" , "javax.swing.plaf.metal.MetalComboBoxUI" , "removeListeners") , Matchers . anything ()) ;
  }
}
