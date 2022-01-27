package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_ScrollPaneTest {

  @Test
  void layout() throws Exception {
    assertThat(synthesiseAlias("java.awt.ScrollPane", "layout"), contains(".doLayout("));
  }
}
