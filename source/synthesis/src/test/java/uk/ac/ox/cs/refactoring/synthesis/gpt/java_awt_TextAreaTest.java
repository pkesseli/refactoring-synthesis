
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_TextAreaTest {
  @Test
  void appendText() throws Exception {
    assertThat(synthesiseGPT("this.appendText(a);\n", "this.append(a);\n", "java.awt.TextArea", "appendText", "java.lang.String"), anyOf(contains("append")));
  }

  @Test
  void insertText() throws Exception {
    assertThat(synthesiseGPT("this.insertText(a, b);\n\n", "this.insert(a, b);\n", "java.awt.TextArea", "insertText", "java.lang.String", "int"), anyOf(contains("insert")));
  }

  @Test
  void minimumSize1() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize();\n\n", "this.getMinimumSize();\n", "java.awt.TextArea", "minimumSize"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void minimumSize2() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize(a, b);\n\n", "this.getMinimumSize(a, b);\n", "java.awt.TextArea", "minimumSize", "int", "int"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void preferredSize1() throws Exception {
    assertThat(synthesiseGPT("Dimension preferredSize = this.preferredSize();\n\n", "Dimension preferredSize = this.getPreferredSize();\n", "java.awt.TextArea", "preferredSize"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void preferredSize2() throws Exception {
    assertThat(synthesiseGPT("this.preferredSize(a, b);\n\n", "this.getPreferredSize(a, b);\n", "java.awt.TextArea", "preferredSize", "int", "int"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void replaceText() throws Exception {
    assertThat(synthesiseGPT("this.replaceText(a, b, c);\n\n", "this.replaceRange(a, b, c);\n", "java.awt.TextArea", "replaceText", "java.lang.String", "int", "int"), anyOf(contains("replaceRange")));
  }
}