
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_security_ProviderTest {
    
  @Test
  @Disabled("Return types differ for the two methods. Presumably simple refactoring, but we just don't support this (yet?).")
  void getVersion() throws Exception {
    assertThat(synthesiseAlias("java.security.Provider", "getVersion"),
        contains("getVersionStr"));
  }
}
