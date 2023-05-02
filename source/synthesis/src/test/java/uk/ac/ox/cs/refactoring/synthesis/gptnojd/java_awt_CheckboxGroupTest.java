
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_CheckboxGroupTest {
  @Test
  void getCurrent() throws Exception {
    assertThat(synthesiseGPT("CheckboxGroup group = new CheckboxGroup();\nCheckbox checkbox1 = new Checkbox(\"Option 1\", group, true);\nCheckbox checkbox2 = new Checkbox(\"Option 2\", group, false);\nCheckbox currentCheckbox = group.getCurrent();\n\n", "CheckboxGroup group = new CheckboxGroup();\nCheckbox checkbox1 = new Checkbox(\"Option 1\", group, true);\nCheckbox checkbox2 = new Checkbox(\"Option 2\", group, false);\nCheckbox currentCheckbox = null;\nCheckbox[] checkboxes = group.getCheckboxGroup().getElements();\nfor (Checkbox checkbox : checkboxes) {\n    if (checkbox.getState()) {\n        currentCheckbox = checkbox;\n        break;\n    }\n}\n", "java.awt.CheckboxGroup", "getCurrent"), anyOf(contains("getSelectedCheckbox")));
  }

  @Test
  void setCurrent() throws Exception {
    assertThat(synthesiseGPT("Checkbox a = new Checkbox(\"Option A\");\nCheckbox b = new Checkbox(\"Option B\");\nCheckboxGroup group = new CheckboxGroup();\ngroup.setCurrent(a);\n\n", "Checkbox a = new Checkbox(\"Option A\");\nCheckbox b = new Checkbox(\"Option B\");\nCheckboxGroup group = new CheckboxGroup();\na.setState(true);\n", "java.awt.CheckboxGroup", "setCurrent", "java.awt.CheckBox"), anyOf(contains("setSelectedCheckbox")));
  }
}