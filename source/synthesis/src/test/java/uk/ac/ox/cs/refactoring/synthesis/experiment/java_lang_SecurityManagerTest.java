
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_lang_SecurityManagerTest {

  @Test
  @Disabled("Requires mapping an InetAddress to a policy, not possible with the minimal instruction set.")
  void checkMulticast() throws Exception {
    assertThat(synthesiseAlias("java.lang.SecurityManager", "checkMulticast"), contains(".checkPermission("));
  }
}
