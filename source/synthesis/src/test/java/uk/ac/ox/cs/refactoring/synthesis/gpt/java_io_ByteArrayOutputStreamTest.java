
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_io_ByteArrayOutputStreamTest {
  @Test
  void toString1() throws Exception {
    assertThat(synthesiseGPT("String str = this.toString(a);\n\n", "String str = new String(this.toByteArray(), StandardCharsets.UTF_8);\n", "java.io.ByteArrayOutputStream", "toString", "int"), anyOf(contains("toString")));
  }
}