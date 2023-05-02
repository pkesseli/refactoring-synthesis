
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_CharacterTest {
  @Test
  void isJavaLetter() throws Exception {
    assertThat(synthesiseGPT("this.isJavaLetter(a);\n\n", "Character.isJavaIdentifierStart(a);\n", "java.lang.Character", "isJavaLetter", "char"), Matchers.anything());
  }

  @Test
  void isJavaLetterOrDigit() throws Exception {
    assertThat(synthesiseGPT("if (Character.isJavaLetterOrDigit(a)) {\n    // do something\n}\n\n", "if (Character.isJavaIdentifierPart(a)) {\n    // do something\n}\n", "java.lang.Character", "isJavaLetterOrDigit", "char"), Matchers.anything());
  }

  @Test
  void isSpace() throws Exception {
    assertThat(synthesiseGPT("boolean isSpace = Character.isSpace(a);\n\n", "boolean isSpace = Character.isWhitespace(a);\n", "java.lang.Character", "isSpace", "char"), Matchers.anything());
  }
}