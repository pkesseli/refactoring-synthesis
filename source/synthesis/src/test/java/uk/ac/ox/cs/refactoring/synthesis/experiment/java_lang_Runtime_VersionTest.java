
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_lang_Runtime_VersionTest {

  @Test
  void major() throws Exception {
    assertThat(synthesiseAlias("java.lang.Runtime$Version", "major"), contains(".feature("));
  }

  @Test
  void minor() throws Exception {
    assertThat(synthesiseAlias("java.lang.Runtime$Version", "minor"), contains(".interim("));
  }

  @Test
  void security() throws Exception {
    assertThat(synthesiseAlias("java.lang.Runtime$Version", "security"), contains(".update("));
  }
}
