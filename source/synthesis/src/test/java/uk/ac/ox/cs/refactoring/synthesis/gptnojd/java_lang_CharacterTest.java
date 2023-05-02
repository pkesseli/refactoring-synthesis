
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_CharacterTest {
  @Test
  void isJavaLetter() throws Exception {
    assertThat(synthesiseGPT("if (Character.isJavaLetter(a)) {\n    // do something\n}\n\n", "if (Character.isLetter(a) && Character.isAlphabetic(a)) {\n    // do something\n}\n", "java.lang.Character", "isJavaLetter", "char"), Matchers.anything());
  }

  @Test
  void isJavaLetterOrDigit() throws Exception {
    assertThat(synthesiseGPT("if (Character.isJavaLetterOrDigit(a)) {\n    // do something\n}\n\n", "if (Character.isLetterOrDigit(a)) {\n    // do something\n}\n", "java.lang.Character", "isJavaLetterOrDigit", "char"), Matchers.anything());
  }

  @Test
  void isSpace() throws Exception {
    assertThat(synthesiseGPT("if (Character.isSpace(a)) {\n    // do something\n}\n\n", "if (Character.isWhitespace(a)) {\n    // do something\n}\n", "java.lang.Character", "isSpace", "char"), Matchers.anything());
  }
}