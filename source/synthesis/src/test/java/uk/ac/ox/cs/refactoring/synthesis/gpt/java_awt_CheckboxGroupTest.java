
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_CheckboxGroupTest {
  @Test
  void getCurrent() throws Exception {
    assertThat(synthesiseGPT("Checkbox selectedCheckbox = this.getCurrent();\n\n", "Checkbox selectedCheckbox = this.getSelectedCheckbox();\n", "java.awt.CheckboxGroup", "getCurrent"), anyOf(contains("getSelectedCheckbox")));
  }

  @Test
  void setCurrent() throws Exception {
    assertThat(synthesiseGPT("this.setCurrent(a);\n", "a.setState(true);\n", "java.awt.CheckboxGroup", "setCurrent", "java.awt.CheckBox"), anyOf(contains("setSelectedCheckbox")));
  }
}