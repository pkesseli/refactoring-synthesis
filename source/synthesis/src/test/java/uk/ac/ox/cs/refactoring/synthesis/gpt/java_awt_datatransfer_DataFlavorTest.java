
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_datatransfer_DataFlavorTest {
  @Test
  void equals() throws Exception {
    assertThat(synthesiseGPT("this.equals(a);\n", "this.isMimeTypeEqual(a.getMimeType());\n", "java.awt.datatransfer.DataFlavor", "equals", "java.lang.String"), anyOf(contains("hashCode"), contains("isMimeTypeEqual")));
  }

  @Test
  void normalizeMimeType() throws Exception {
    assertThat(synthesiseGPT("String normalizedMimeType = normalizeMimeType(a);\n\n", "String normalizedMimeType = DataFlavor.selectBestTextFlavor(DataFlavor.getTextFlavor(a)).getMimeType();\n", "java.awt.datatransfer.DataFlavor", "normalizeMimeType", "java.lang.String"), Matchers.anything());
  }

  @Test
  void normalizeMimeTypeParameter() throws Exception {
    assertThat(synthesiseGPT("String normalized = DataFlavor.normalizeMimeTypeParameter(a, b);\n\n", "String normalized = a + \"=\" + b;\n", "java.awt.datatransfer.DataFlavor", "normalizeMimeTypeParameter", "java.lang.String", "java.lang.String"), Matchers.anything());
  }
}