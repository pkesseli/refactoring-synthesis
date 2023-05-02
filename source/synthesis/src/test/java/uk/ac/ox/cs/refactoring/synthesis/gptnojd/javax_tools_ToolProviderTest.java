
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_tools_ToolProviderTest {
  @Test
  void getSystemToolClassLoader() throws Exception {
    assertThat(synthesiseGPT("ClassLoader classLoader = this.getSystemToolClassLoader();\n\n", "ClassLoader classLoader = ClassLoader.getSystemClassLoader();\n", "javax.tools.ToolProvider", "getSystemToolClassLoader"), anyOf(contains("service loader"), contains("system tool provider")));
  }
}