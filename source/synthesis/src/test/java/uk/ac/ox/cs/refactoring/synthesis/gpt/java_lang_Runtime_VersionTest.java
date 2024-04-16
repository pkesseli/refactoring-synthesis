
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_Runtime_VersionTest {
  @Test
  void major() throws Exception {
assertThat (synthesiseNeural ("major" , "this.major();" , "\nthis.version().feature();\n" , "java.lang.Runtime$Version" , "major") , anyOf (contains ("feature"))) ;
  }

  @Test
  void minor() throws Exception {
assertThat (synthesiseNeural ("minor" , "this.minor();" , "\nthis.version().interim();\n" , "java.lang.Runtime$Version" , "minor") , anyOf (contains ("interim"))) ;
  }

  @Test
  void security() throws Exception {
assertThat (synthesiseNeural ("security" , "this.security();" , "\nthis.version().update();\n" , "java.lang.Runtime$Version" , "security") , anyOf (contains ("update"))) ;
  }
}
