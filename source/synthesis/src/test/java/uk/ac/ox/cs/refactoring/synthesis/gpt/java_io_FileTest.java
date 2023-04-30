
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_io_FileTest {
  @Test
  void toURL() throws Exception {
    assertThat(synthesiseGPT("URL url = file.toURL();\n\n", "URL url = file.toURI().toURL();\n", "java.io.File", "toURL"), anyOf(contains("toURI"), contains("toURL")));
  }
}