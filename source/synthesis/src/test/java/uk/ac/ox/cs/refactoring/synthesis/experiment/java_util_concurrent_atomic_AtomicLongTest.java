
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicLongTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseAlias("java.util.concurrent.atomic.AtomicLong", "weakCompareAndSet", "long", "long"),
        contains("weakCompareAndSetPlain"));
  }
}
        