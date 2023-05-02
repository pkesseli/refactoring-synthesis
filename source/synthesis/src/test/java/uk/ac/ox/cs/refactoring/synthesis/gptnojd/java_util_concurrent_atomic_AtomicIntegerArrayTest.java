
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicIntegerArrayTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseGPT("this.weakCompareAndSet(a, b, c);\n", "AtomicIntegerArray arr = new AtomicIntegerArray(1);\nint prev;\ndo {\n    prev = arr.get(0);\n} while (!arr.compareAndSet(0, prev, (prev == a) ? c : prev));\n", "java.util.concurrent.atomic.AtomicIntegerArray", "weakCompareAndSet", "int", "int", "int"), anyOf(contains("weakCompareAndSetPlain")));
  }
}