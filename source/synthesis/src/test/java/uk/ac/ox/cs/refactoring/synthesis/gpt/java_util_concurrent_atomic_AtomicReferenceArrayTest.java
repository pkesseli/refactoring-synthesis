
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicReferenceArrayTest {
  @Test
  void weakCompareAndSet() throws Exception {
assertThat (synthesiseGPT ("weakCompareAndSet" , "this.weakCompareAndSet(param0, param1, param2);" , "\nthis.compareAndSet(param0, param1, param2);\n" , "java.util.concurrent.atomic.AtomicReferenceArray" , "weakCompareAndSet" , "int" , "E" , "E") , anyOf (contains ("weakCompareAndSetPlain"))) ;
  }
}
