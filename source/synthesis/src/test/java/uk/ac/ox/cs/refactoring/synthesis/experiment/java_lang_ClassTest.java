
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_lang_ClassTest {

  @Test
  void newInstance() throws Exception {
    assertThat(synthesiseAlias("java.lang.Class", "newInstance"),
        allOf(contains(".getDeclaredConstructor("), contains(".newInstance(")));
  }
}
