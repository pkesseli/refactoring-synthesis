package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.testAlias;

import org.junit.jupiter.api.Test;

public class CheckboxGroupTest {
  @Test
  void getCurrent() throws Exception {
    testAlias(".getSelectedCheckbox(", "java.awt.CheckboxGroup", "getCurrent");
  }

  @Test
  void setCurrent() throws Exception {
    testAlias(".setSelectedCheckbox(", "java.awt.CheckboxGroup", "setCurrent", "java.awt.Checkbox");
  }
}
