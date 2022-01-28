
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_lang_invoke_MethodHandles_LookupTest {

  @Test
  void hasPrivateAccess() throws Exception {
    assertThat(synthesiseAlias("java.lang.invoke.MethodHandles$Lookup", "hasPrivateAccess"),
        contains(".hasFullPrivilegeAccess("));
  }
}
