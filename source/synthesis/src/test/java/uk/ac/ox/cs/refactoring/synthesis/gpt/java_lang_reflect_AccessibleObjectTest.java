
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_reflect_AccessibleObjectTest {
  @Test
  void isAccessible() throws Exception {
assertThat (synthesiseGPT ("isAccessible" , "this.isAccessible();" , "" , "java.lang.reflect.AccessibleObject" , "isAccessible") , anyOf (contains ("canAccess") , contains ("false"))) ;
  }
}
