
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_imageio_stream_FileImageInputStreamTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public void cleanup() throws IOException {\n    // some code here\n    this.finalize();\n}\n\n", "public void cleanup() throws IOException {\n    // some code here\n    // alternative cleanup mechanism\n}\n", "javax.imageio.stream.FileImageInputStream", "finalize"), anyOf(contains("finalize")));
  }
}