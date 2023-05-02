
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicLongTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseGPT("this.weakCompareAndSet(a, b);\n", "AtomicLong atomicLong = new AtomicLong(a);\natomicLong.weakCompareAndSet(a, b);\n", "java.util.concurrent.atomic.AtomicLong", "weakCompareAndSet", "long", "long"), anyOf(contains("weakCompareAndSetPlain")));
  }
}