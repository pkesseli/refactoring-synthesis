
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_io_ObjectInputStreamTest {
  @Test
  void readLine() throws Exception {
    assertThat(synthesiseGPT("String line = this.readLine();\n\n", "BufferedReader reader = new BufferedReader(new InputStreamReader(this));\nString line = reader.readLine();\n", "java.io.ObjectInputStream", "readLine"), Matchers.anything());
  }
}