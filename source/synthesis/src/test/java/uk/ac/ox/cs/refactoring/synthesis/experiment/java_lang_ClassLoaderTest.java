
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_lang_ClassLoaderTest {
  @Test
  void getPackage() throws Exception {
    assertThat(synthesiseAlias("java.lang.ClassLoader", "getPackage", "java.lang.String"),
        contains("getDefinedPackage"));
  }
}
        