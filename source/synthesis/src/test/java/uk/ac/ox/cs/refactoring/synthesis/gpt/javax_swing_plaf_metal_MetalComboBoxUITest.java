
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_metal_MetalComboBoxUITest {
  @Disabled("No replacement")
  @Test
  void editablePropertyChanged() throws Exception {
assertThat (synthesiseGPT ("editablePropertyChanged" , "this.editablePropertyChanged(param0);" , "\n// The method is deprecated and no longer used in Java 2 platform v1.4\n// You can safely remove this method call\n;" , "javax.swing.plaf.metal.MetalComboBoxUI" , "editablePropertyChanged" , "java.beans.PropertyChangeEvent") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void removeListeners() throws Exception {
assertThat (synthesiseGPT ("removeListeners" , "this.removeListeners();" , "\nfinal ComboBoxEditor editor = comboBox.getEditor();\nif (editor instanceof BasicComboBoxEditor) {\n    final JTextField textField = ((BasicComboBoxEditor) editor).getEditorComponent();\n    final Document document = textField.getDocument();\n    if (document instanceof AbstractDocument) {\n        ((AbstractDocument) document).removeDocumentListener(this);\n    }\n}\n" , "javax.swing.plaf.metal.MetalComboBoxUI" , "removeListeners") , Matchers . anything ()) ;
  }
}
