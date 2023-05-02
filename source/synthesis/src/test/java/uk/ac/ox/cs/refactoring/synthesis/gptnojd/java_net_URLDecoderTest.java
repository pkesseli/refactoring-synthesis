
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLDecoderTest {
  @Test
  void decode() throws Exception {
    assertThat(synthesiseGPT("String decodedString = java.net.URLDecoder.decode(encodedString, \"UTF-8\");\n\n", "String decodedString = java.net.URLDecoder.decode(encodedString.replace(\"+\", \"%2B\"), \"UTF-8\");\n", "java.net.URLDecoder", "decode", "java.lang.String"), Matchers.anything());
  }
}