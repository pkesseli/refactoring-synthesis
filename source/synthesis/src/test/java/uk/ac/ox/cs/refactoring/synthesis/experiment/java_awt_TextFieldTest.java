package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_TextFieldTest {

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "minimumSize"), contains(".getMinimumSize("));
  }

  @Test
  void minimumSizeIntInt() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "minimumSize", "int", "int"), contains(".getMinimumSize("));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "preferredSize"), contains(".getPreferredSize("));
  }

  @Test
  void preferredSizeIntInt() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "preferredSize", "int", "int"), contains(".getPreferredSize("));
  }

  @Test
  void setEchoCharacter() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextField", "setEchoCharacter", "char"), contains(".setEchoChar("));
  }
}
