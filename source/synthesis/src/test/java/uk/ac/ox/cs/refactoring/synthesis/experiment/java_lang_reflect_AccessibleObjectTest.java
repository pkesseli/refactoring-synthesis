
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_lang_reflect_AccessibleObjectTest {
  // Signature changed
  @Test
  void isAccessible() throws Exception {
    assertThat(synthesiseAlias("java.lang.reflect.AccessibleObject", "isAccessible"), anyOf(contains("canAccess"), contains("false")));
  }
}