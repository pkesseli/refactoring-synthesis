
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLStreamHandlerTest {
  @Test
  void setURL() throws Exception {
assertThat (synthesiseGPT ("this.setURL(param0, param1, param2, param3, param4, param5);" , "" , "java.net.URLStreamHandler" , "setURL" , "java.net.URL" , "java.lang.String" , "java.lang.String" , "int" , "java.lang.String" , "java.lang.String") , Matchers . anything ()) ;
  }
}
