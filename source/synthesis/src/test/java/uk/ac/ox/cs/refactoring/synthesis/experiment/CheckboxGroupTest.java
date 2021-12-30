package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

public class CheckboxGroupTest {

  @Test
  void getCurrent() throws Exception {
    assertThat(synthesiseAlias("java.awt.CheckboxGroup", "getCurrent"), contains(".getSelectedCheckbox("));
  }

  @Test
  void setCurrent() throws Exception {
    assertThat(synthesiseAlias("java.awt.CheckboxGroup", "setCurrent", "java.awt.Checkbox"),
        contains(".setSelectedCheckbox("));
  }
}
