
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicReferenceTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseGPT("this.weakCompareAndSet(a, b);\n\n", "this.weakCompareAndSetPlain(a, b);\n", "java.util.concurrent.atomic.AtomicReference", "weakCompareAndSet", "V", "V"), anyOf(contains("compareAndExchange"), contains("compareAndSet"), contains("weakCompareAndSetPlain")));
  }
}