
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_image_ColorModelTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public void cleanup() {\n    // perform some cleanup\n    this.finalize();\n}\n\n", "public void cleanup() {\n    // perform some cleanup\n    // alternative cleanup mechanism\n}\n", "java.awt.image.ColorModel", "finalize"), anyOf(contains("finalize")));
  }
}