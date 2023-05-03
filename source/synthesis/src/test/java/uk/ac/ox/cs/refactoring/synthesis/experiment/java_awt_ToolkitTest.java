package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_ToolkitTest {

  @Test
  void getFontMetrics() throws Exception {
    assertThat(synthesiseAlias("java.awt.Toolkit", "getFontMetrics", "java.awt.Font"), contains(".getLineMetrics("));
  }
}
