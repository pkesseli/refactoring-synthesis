package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_TextAreaTest {

  @Test
  void appendText() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "appendText", "java.lang.String"), contains(".append("));
  }

  @Test
  void insertText() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "insertText", "java.lang.String", "int"), contains(".insert("));
  }

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "minimumSize"), contains(".getMinimumSize("));
  }

  @Test
  void minimumSizeIntInt() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "minimumSize", "int", "int"), contains(".getMinimumSize("));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "preferredSize"), contains(".getPreferredSize("));
  }

  @Test
  void preferredSizeIntInt() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "preferredSize", "int", "int"), contains(".getPreferredSize("));
  }

  @Test
  void replaceText() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "replaceText", "java.lang.String", "int", "int"),
        contains(".replaceRange("));
  }
}
