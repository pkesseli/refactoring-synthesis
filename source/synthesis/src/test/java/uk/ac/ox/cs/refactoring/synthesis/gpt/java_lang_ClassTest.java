
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ClassTest {
  @Test
  void newInstance() throws Exception {
assertThat (synthesiseGPT ("this.newInstance();" , "\nthis.getDeclaredConstructor().newInstance()\n;" , "java.lang.Class" , "newInstance") , anyOf (contains ("InvocationTargetException") , contains ("NoSuchMethodException") , contains ("ReflectiveOperationException") , contains ("newInstance"))) ;
  }
}
