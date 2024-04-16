
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_concurrent_atomic_AtomicIntegerTest {
  @Test
  void weakCompareAndSet() throws Exception {
assertThat (synthesiseNeural ("weakCompareAndSet" , "this.weakCompareAndSet(param0, param1);" , "\nthis.weakCompareAndSetPlain(param0, param1);\n" , "java.util.concurrent.atomic.AtomicInteger" , "weakCompareAndSet" , "int" , "int") , anyOf (contains ("weakCompareAndSetPlain"))) ;
  }
}
