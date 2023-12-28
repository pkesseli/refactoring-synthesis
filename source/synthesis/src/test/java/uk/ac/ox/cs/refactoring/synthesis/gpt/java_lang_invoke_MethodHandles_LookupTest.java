
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_invoke_MethodHandles_LookupTest {
  @Test
  void hasPrivateAccess() throws Exception {
assertThat (synthesiseGPT ("hasPrivateAccess" , "this.hasPrivateAccess();" , "\nthis.hasFullPrivilegeAccess();\n" , "java.lang.invoke.MethodHandles.Lookup" , "hasPrivateAccess") , anyOf (contains ("MODULE") , contains ("PRIVATE") , contains ("hasFullPrivilegeAccess"))) ;
  }
}
