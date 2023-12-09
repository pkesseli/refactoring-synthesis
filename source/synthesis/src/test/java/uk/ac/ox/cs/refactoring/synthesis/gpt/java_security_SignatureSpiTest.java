
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_SignatureSpiTest {
  @Test
  void engineGetParameter() throws Exception {
assertThat (synthesiseGPT ("engineGetParameter" , "this.engineGetParameter(param0);" , "" , "java.security.SignatureSpi" , "engineGetParameter" , "java.lang.String") , Matchers . anything ()) ;
  }

  @Test
  void engineSetParameter() throws Exception {
assertThat (synthesiseGPT ("engineSetParameter" , "this.engineSetParameter(param0, param1);" , "" , "java.security.SignatureSpi" , "engineSetParameter" , "java.lang.String" , "java.lang.Object") , anyOf (contains ("engineSetParameter"))) ;
  }
}
