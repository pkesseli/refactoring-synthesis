
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_net_URLDecoderTest {
  @Test
  void decode() throws Exception {
    // For non-decoded characters a no-op, too few counterexamples
    assertThat(synthesiseAlias("java.net.URLDecoder", "decode", "java.lang.String"), contains("decode"));
  }
}