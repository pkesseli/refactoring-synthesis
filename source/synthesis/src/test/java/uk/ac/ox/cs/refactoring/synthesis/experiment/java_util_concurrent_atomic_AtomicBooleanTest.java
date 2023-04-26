
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicBooleanTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseAlias("java.util.concurrent.atomic.AtomicBoolean", "weakCompareAndSet", "boolean", "boolean"),
        contains("weakCompareAndSetPlain"));
  }
}
        