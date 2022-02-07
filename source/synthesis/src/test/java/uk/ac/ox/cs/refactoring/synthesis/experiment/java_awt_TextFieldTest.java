package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAliasBenchmark;

import org.junit.jupiter.api.Test;

class java_awt_TextFieldTest {

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "minimumSize"), contains(".getMinimumSize("));
  }

  @Test
  void minimumSizeInt() throws Exception {
    assertThat(synthesiseAliasBenchmark("minimumSizeInt", "java.awt.TextField", "minimumSize", "int"), contains(".getMinimumSize("));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "preferredSize"), contains(".getPreferredSize("));
  }

  @Test
  void preferredSizeInt() throws Exception {
    assertThat(synthesiseAliasBenchmark("preferredSizeInt", "java.awt.TextField", "preferredSize", "int"), contains(".getPreferredSize("));
  }

  @Test
  void setEchoCharacter() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "setEchoCharacter", "char"), contains(".setEchoChar("));
  }
}
