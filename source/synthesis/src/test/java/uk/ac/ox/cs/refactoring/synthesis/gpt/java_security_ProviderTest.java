
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_ProviderTest {
  @Test
  void getVersion() throws Exception {
assertThat (synthesiseGPT ("getVersion" , "this.getVersion();" , "\nthis.getVersionStr()\n;" , "java.security.Provider" , "getVersion") , anyOf (contains ("getVersionStr"))) ;
  }
}
