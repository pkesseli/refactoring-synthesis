package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAliasBenchmark;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_TextAreaTest {

  @Test
  void appendText() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "appendText", "java.lang.String"), contains(".append("));
  }

  @Disabled("Crashed VM on last execution.")
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
    assertThat(synthesiseAliasBenchmark("minimumSizeIntInt", "java.awt.TextArea", "minimumSize", "int", "int"),
        contains(".getMinimumSize("));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "preferredSize"), contains(".getPreferredSize("));
  }

  @Test
  void preferredSizeIntInt() throws Exception {
    assertThat(synthesiseAliasBenchmark("preferredSizeIntInt", "java.awt.TextArea", "preferredSize", "int", "int"),
        contains(".getPreferredSize("));
  }

  @Test
  void replaceText() throws Exception {
    assertThat(synthesiseAlias("java.awt.TextArea", "replaceText", "java.lang.String", "int", "int"),
        contains(".replaceRange("));
  }

  @Test
  void appendTextGPT() throws Exception {
    assertThat(synthesiseGPT("{ TextArea textArea = new TextArea(); String newText = \"New text to append\"; textArea.appendText(newText);}", "{ TextArea textArea = new TextArea(); String newText = \"New text to append\"; textArea.append(newText);}", "java.awt.TextArea", "appendText", "java.lang.String"), contains(".append("));
  }
}
