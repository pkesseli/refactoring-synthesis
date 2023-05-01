
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_imageio_stream_FileImageOutputStreamTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public void cleanup() {\n    try {\n        this.finalize();\n    } catch (Throwable t) {\n        // handle exception\n    }\n}\n", "public void cleanup() {\n    // alternative cleanup mechanism\n}\n", "javax.imageio.stream.FileImageOutputStream", "finalize"), anyOf(contains("finalize")));
  }
}