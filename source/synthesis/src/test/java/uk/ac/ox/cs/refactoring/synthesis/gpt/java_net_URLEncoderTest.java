
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLEncoderTest {
  @Test
  void encode() throws Exception {
    assertThat(synthesiseGPT("String encodedString = URLEncoder.encode(a);\n\n", "String encodedString = URLEncoder.encode(a, StandardCharsets.UTF_8.toString());\n", "java.net.URLEncoder", "encode", "java.lang.String"), Matchers.anything());
  }
}