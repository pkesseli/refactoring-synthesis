
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_Runtime_VersionTest {
  @Test
  void major() throws Exception {
    assertThat(synthesiseGPT("int majorVersion = Runtime.version().major();\n\n", "int majorVersion = Runtime.version().feature();\n", "java.lang.Runtime$Version", "major"), anyOf(contains("feature")));
  }

  @Test
  void minor() throws Exception {
    assertThat(synthesiseGPT("int minorVersion = Runtime.Version.current().minor();\n\n", "int minorVersion = Runtime.Version.current().feature();\n", "java.lang.Runtime$Version", "minor"), anyOf(contains("interim")));
  }

  @Test
  void security() throws Exception {
    assertThat(synthesiseGPT("Runtime.Version version = Runtime.Version.parse(\"11.0.2\");\nString security = version.security();\n\n", "Runtime.Version version = Runtime.Version.parse(\"11.0.2\");\nString security = version.feature() == 11 ? version.interim() + \".\" + version.update() + \".\" + version.patch() + \".\" + version.build() : null;\n", "java.lang.Runtime$Version", "security"), anyOf(contains("update")));
  }
}