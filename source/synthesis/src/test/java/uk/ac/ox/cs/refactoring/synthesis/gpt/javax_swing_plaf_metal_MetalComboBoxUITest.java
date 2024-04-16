
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_metal_MetalComboBoxUITest {
  @Disabled("No replacement")
  @Test
  void editablePropertyChanged() throws Exception {
assertThat (synthesiseNeural ("editablePropertyChanged" , "this.editablePropertyChanged(param0);" , "" , "javax.swing.plaf.metal.MetalComboBoxUI" , "editablePropertyChanged" , "java.beans.PropertyChangeEvent") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void removeListeners() throws Exception {
assertThat (synthesiseNeural ("removeListeners" , "this.removeListeners();" , "" , "javax.swing.plaf.metal.MetalComboBoxUI" , "removeListeners") , Matchers . anything ()) ;
  }
}
