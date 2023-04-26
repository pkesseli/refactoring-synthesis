
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_util_PropertiesTest {

  @Test
  void save() throws Exception {
    assertThat(synthesiseAlias("java.util.Properties", "save", "java.io.OutputStream", "java.lang.String"),
        anyOf(contains(".storeToXML"), contains(".store(")));
  }
}
