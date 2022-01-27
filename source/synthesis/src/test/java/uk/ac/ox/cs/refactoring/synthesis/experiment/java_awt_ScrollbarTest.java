package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_ScrollbarTest {

  @Test
  void getLineIncrement() throws Exception {
    assertThat(synthesiseAlias("java.awt.Scrollbar", "getLineIncrement"), contains(".getUnitIncrement("));
  }

  @Test
  void getPageIncrement() throws Exception {
    assertThat(synthesiseAlias("java.awt.Scrollbar", "getPageIncrement"), contains(".getBlockIncrement("));
  }

  @Test
  void getVisible() throws Exception {
    assertThat(synthesiseAlias("java.awt.Scrollbar", "getVisible"), contains(".getVisibleAmount("));
  }

  @Test
  void setLineIncrement() throws Exception {
    assertThat(synthesiseAlias("java.awt.Scrollbar", "setLineIncrement", "int"), contains(".setUnitIncrement("));
  }

  @Test
  void setPageIncrement() throws Exception {
    assertThat(synthesiseAlias("java.awt.Scrollbar", "setPageIncrement", "int"), contains(".setBlockIncrement("));
  }
}
