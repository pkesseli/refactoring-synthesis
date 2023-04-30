
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_io_DataInputStreamTest {
  @Test
  void readLine() throws Exception {
    assertThat(synthesiseGPT("DataInputStream in = new DataInputStream(socket.getInputStream());\nString line = in.readLine();\n\n", "BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));\nString line = in.readLine();\n", "java.io.DataInputStream", "readLine"), anyOf(contains("BufferedReader"), contains("DataInputStream"), contains("readLine")));
  }
}