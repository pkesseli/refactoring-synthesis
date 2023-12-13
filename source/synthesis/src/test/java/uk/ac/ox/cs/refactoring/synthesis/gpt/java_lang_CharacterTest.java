
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
assertThat (synthesiseGPT ("isJavaLetter" , "this.isJavaLetter(param0);" , "\nCharacter.isJavaIdentifierPart(param0)\n;" , "java.lang.Character" , "isJavaLetter" , "char") , Matchers . anything ()) ;
  }

  @Test
  void isJavaLetterOrDigit() throws Exception {
assertThat (synthesiseGPT ("isJavaLetterOrDigit" , "this.isJavaLetterOrDigit(param0);" , "\nCharacter.isLetterOrDigit(param0)\n;" , "java.lang.Character" , "isJavaLetterOrDigit" , "char") , Matchers . anything ()) ;
  }

  @Test
  void isSpace() throws Exception {
assertThat (synthesiseGPT ("isSpace" , "this.isSpace(param0);" , "\nCharacter.isWhitespace(param0)\n;" , "java.lang.Character" , "isSpace" , "char") , Matchers . anything ()) ;
  }
}
