
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_PropertiesTest {
  @Test
  void save() throws Exception {
    assertThat(synthesiseGPT("this.save(a, b);\n\n", "try (OutputStream out = new FileOutputStream(a)) {\n    this.store(out, b);\n}\n", "java.util.Properties", "save", "java.io.OutputStream", "java.lang.String"), anyOf(contains("store"), contains("storeToXML")));
  }
}