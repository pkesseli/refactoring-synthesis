
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
assertThat (synthesiseGPT ("editablePropertyChanged" , "this.editablePropertyChanged(param0);" , "" , "javax.swing.plaf.metal.MetalComboBoxUI" , "editablePropertyChanged" , "java.beans.PropertyChangeEvent") , Matchers . anything ()) ;
  }

  @Test
  void removeListeners() throws Exception {
assertThat (synthesiseGPT ("removeListeners" , "this.removeListeners();" , "\nimport javax.swing.plaf.metal.MetalComboBoxUI;\nimport javax.swing.JComboBox;\n\npublic class MyMetalComboBoxUI extends MetalComboBoxUI {\n    @Override\n    public void uninstallUI(JComponent c) {\n        removeCustomListeners();\n        super.uninstallUI(c);\n    }\n\n    private void removeCustomListeners() {\n        // Hypothetical example of removing listeners manually\n        // You would need to know which listeners to remove\n        if (comboBox != null) {\n            // For example, if a PropertyChangeListener was added, you would remove it like this:\n            // comboBox.removePropertyChangeListener(propertyChangeListener);\n            // Repeat for other listener types (e.g., ItemListener, ActionListener, etc.)\n        }\n    }\n}\n\n// Usage\nJComboBox comboBox = new JComboBox();\ncomboBox.setUI(new MyMetalComboBoxUI());\n" , "javax.swing.plaf.metal.MetalComboBoxUI" , "removeListeners") , Matchers . anything ()) ;
  }
}
