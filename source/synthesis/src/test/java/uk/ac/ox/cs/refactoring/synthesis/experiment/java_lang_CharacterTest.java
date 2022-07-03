
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_lang_CharacterTest {

  @Test
  void isJavaLetter() throws Exception {
    assertThat(synthesiseAlias("java.lang.Character", "isJavaLetter", "char"),
        anyOf(contains(".isJavaIdentifierStart("), contains(".isUnicodeIdentifierStart(")));
  }

  @Test
  void isJavaLetterOrDigit​() throws Exception {
    assertThat(synthesiseAlias("java.lang.Character", "isJavaLetterOrDigit", "char"),
        contains(".isJavaIdentifierPart("));
  }

  @Test
  void isSpace() throws Exception {
    assertThat(synthesiseAlias("java.lang.Character", "isSpace", "char"), contains(".isWhitespace("));
  }
}
