package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_ContainerTest {

  @Test
  void countComponents() throws Exception {
    assertThat(synthesiseAlias("java.awt.Container", "countComponents"), contains(".getComponentCount("));
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void deliverEvent() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "deliverEvent​", "java.awt.Event"), contains(".dispatchEvent("));
  }

  @Test
  void insets() throws Exception {
    assertThat(synthesiseAlias("java.awt.Container", "insets"), contains(".getInsets("));
  }

  @Test
  void layout() throws Exception {
    assertThat(synthesiseAlias("java.awt.Container", "layout"), contains(".doLayout("));
  }

  @Test
  void locate() throws Exception {
    assertThat(synthesiseAlias("java.awt.Container", "locate", "int", "int"), contains(".getComponentAt("));
  }

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.Container", "minimumSize"), contains(".getMinimumSize("));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.Container", "preferredSize"), contains(".getPreferredSize("));
  }
}
