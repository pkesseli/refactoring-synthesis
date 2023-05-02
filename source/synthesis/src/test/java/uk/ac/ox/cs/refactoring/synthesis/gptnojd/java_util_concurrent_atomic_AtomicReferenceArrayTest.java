
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicReferenceArrayTest {
  @Test
  void weakCompareAndSet() throws Exception {
    assertThat(synthesiseGPT("this.weakCompareAndSet(a, b, c);\n", "AtomicReferenceArray<E> arr = new AtomicReferenceArray<>(1);\narr.set(0, b);\nAtomicReferenceArray<E> oldArr = new AtomicReferenceArray<>(1);\noldArr.set(0, c);\nAtomicReferenceArray<E> newArr = new AtomicReferenceArray<>(1);\nnewArr.set(0, b);\nthis.compareAndSet(a, oldArr, newArr);\n", "java.util.concurrent.atomic.AtomicReferenceArray", "weakCompareAndSet", "int", "E", "E"), anyOf(contains("weakCompareAndSetPlain")));
  }
}