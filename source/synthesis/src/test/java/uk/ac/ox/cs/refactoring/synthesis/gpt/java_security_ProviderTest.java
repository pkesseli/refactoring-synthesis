
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
assertThat (synthesiseGPT ("this.getVersion();" , "\nthis.getVersionStr()\n```\n\nIf you need the version as a `double` value, you could parse the string:\n\n```java\nDouble.parseDouble(this.getVersionStr())\n;" , "java.security.Provider" , "getVersion") , anyOf (contains ("getVersionStr"))) ;
  }
}
