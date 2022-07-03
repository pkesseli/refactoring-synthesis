package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_CheckboxGroupTest {

  @Test
  void getCurrent() throws Exception {
    try {
      assertThat(synthesiseAlias("java.awt.CheckboxGroup", "getCurrent"), contains(".getSelectedCheckbox("));
    } catch (final Exception e) {
      if (e instanceof org.junit.runners.model.MultipleFailureException) {
        for (final Throwable t : ((org.junit.runners.model.MultipleFailureException) e).getFailures()) {
          t.printStackTrace();
        }
      }
      e.printStackTrace();
      throw e;
    }
  }

  @Test
  void setCurrent() throws Exception {
    assertThat(synthesiseAlias("java.awt.CheckboxGroup", "setCurrent", "java.awt.Checkbox"),
        contains(".setSelectedCheckbox("));
  }
}
