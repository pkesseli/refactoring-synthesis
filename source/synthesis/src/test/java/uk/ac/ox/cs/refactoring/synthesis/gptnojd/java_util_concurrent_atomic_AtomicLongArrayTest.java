
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicLongArrayTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseGPT("this.weakCompareAndSet(a, b, c);\n", "AtomicLongArray array = new AtomicLongArray(1);\nlong currentValue = array.get(a);\nwhile (!array.compareAndSet(a, currentValue, c)) {\n    currentValue = array.get(a);\n}\n", "java.util.concurrent.atomic.AtomicLongArray", "weakCompareAndSet", "int", "long", "long"), anyOf(contains("weakCompareAndSetPlain")));
  }
}