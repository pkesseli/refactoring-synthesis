
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_imageio_stream_MemoryCacheImageInputStreamTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public void cleanup() {\n    try {\n        MemoryCacheImageInputStream input = new MemoryCacheImageInputStream(inputStream);\n        // some code here\n    } finally {\n        this.finalize();\n    }\n}\n", "public void cleanup() {\n    try (MemoryCacheImageInputStream input = new MemoryCacheImageInputStream(inputStream)) {\n        // some code here\n    } catch (IOException e) {\n        // handle exception\n    }\n}\n", "javax.imageio.stream.MemoryCacheImageInputStream", "finalize"), anyOf(contains("finalize")));
  }
}