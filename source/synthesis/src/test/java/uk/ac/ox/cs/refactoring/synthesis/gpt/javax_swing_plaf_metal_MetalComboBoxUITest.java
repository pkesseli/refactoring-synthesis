
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
    assertThat(synthesiseGPT("this.editablePropertyChanged(a);\n\n", "if (\"editable\".equals(a.getPropertyName())) {\n    editableChanged();\n}\n", "javax.swing.plaf.metal.MetalComboBoxUI", "editablePropertyChanged", "java.beans.PropertyChangeEvent"), Matchers.anything());
  }

  @Test
  void removeListeners() throws Exception {
    assertThat(synthesiseGPT("public class MyComboBox extends JComboBox<String> {\n    public MyComboBox() {\n        this.removeListeners();\n    }\n}\n\n", "public class MyComboBox extends JComboBox<String> {\n    public MyComboBox() {\n        for (ActionListener listener : getActionListeners()) {\n            removeActionListener(listener);\n        }\n        for (ItemListener listener : getItemListeners()) {\n            removeItemListener(listener);\n        }\n    }\n}\n", "javax.swing.plaf.metal.MetalComboBoxUI", "removeListeners"), Matchers.anything());
  }
}