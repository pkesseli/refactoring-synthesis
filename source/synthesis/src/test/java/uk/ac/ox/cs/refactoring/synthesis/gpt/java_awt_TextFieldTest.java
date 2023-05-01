
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_TextFieldTest {
  @Test
  void minimumSize1() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize();\n\n", "this.getMinimumSize();\n", "java.awt.TextField", "minimumSize"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void minimumSize2() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize(a);\n\n", "this.getMinimumSize(a);\n", "java.awt.TextField", "minimumSize", "int"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void preferredSize1() throws Exception {
    assertThat(synthesiseGPT("Dimension preferredSize = this.preferredSize();\n\n", "Dimension preferredSize = this.getPreferredSize();\n", "java.awt.TextField", "preferredSize"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void preferredSize2() throws Exception {
    assertThat(synthesiseGPT("this.preferredSize(a);\n\n", "this.getPreferredSize(a);\n", "java.awt.TextField", "preferredSize", "int"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void setEchoCharacter() throws Exception {
    assertThat(synthesiseGPT("this.setEchoCharacter(a);\n", "this.setEchoChar(a);\n", "java.awt.TextField", "setEchoCharacter", "char"), anyOf(contains("setEchoChar")));
  }
}