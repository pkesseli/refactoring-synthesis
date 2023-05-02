
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

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
    assertThat(synthesiseGPT("this.removeListeners();\n", "ComboBoxEditor editor = this.getEditor();\nif (editor != null) {\n    Component editorComponent = editor.getEditorComponent();\n    if (editorComponent instanceof JComponent) {\n        JComponent jEditorComponent = (JComponent) editorComponent;\n        for (FocusListener listener : jEditorComponent.getFocusListeners()) {\n            jEditorComponent.removeFocusListener(listener);\n        }\n        for (KeyListener listener : jEditorComponent.getKeyListeners()) {\n            jEditorComponent.removeKeyListener(listener);\n        }\n        for (MouseListener listener : jEditorComponent.getMouseListeners()) {\n            jEditorComponent.removeMouseListener(listener);\n        }\n    }\n}\n", "javax.swing.plaf.metal.MetalComboBoxUI", "removeListeners"), Matchers.anything());
  }
}