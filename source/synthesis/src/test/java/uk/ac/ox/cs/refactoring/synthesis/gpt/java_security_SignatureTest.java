
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_security_SignatureTest {
  @Disabled("No replacement")
  @Test
  void getParameter() throws Exception {
assertThat (synthesiseGPT ("getParameter" , "this.getParameter(param0);" , "" , "java.security.Signature" , "getParameter" , "java.lang.String") , Matchers . anything ()) ;
  }

  @Test
  void setParameter() throws Exception {
assertThat (synthesiseGPT ("setParameter" , "this.setParameter(param0, param1);" , "\nthis.setParameter(param0, param1);\n" , "java.security.Signature" , "setParameter" , "java.lang.String" , "java.lang.Object") , anyOf (contains ("setParameter"))) ;
  }
}
