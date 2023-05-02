
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_StringTest {
  @Test
  void getBytes() throws Exception {
    assertThat(synthesiseGPT("this.getBytes(a, b, c, d);\n\n", "this.getBytes().get(a, b, c, d);\n", "java.lang.String", "getBytes", "int", "int", "byte[]", "int"), anyOf(contains("getBytes")));
  }
}