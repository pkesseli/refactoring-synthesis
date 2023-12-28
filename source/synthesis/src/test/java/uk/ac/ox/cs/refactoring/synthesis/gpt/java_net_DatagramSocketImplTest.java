
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_DatagramSocketImplTest {
  @Test
  void getTTL() throws Exception {
assertThat (synthesiseGPT ("getTTL" , "this.getTTL();" , "\nthis.getTimeToLive();\n" , "java.net.DatagramSocketImpl" , "getTTL") , anyOf (contains ("getTimeToLive"))) ;
  }

  @Test
  void setTTL() throws Exception {
assertThat (synthesiseGPT ("setTTL" , "this.setTTL(param0);" , "\nthis.setTimeToLive(param0);\n" , "java.net.DatagramSocketImpl" , "setTTL" , "byte") , anyOf (contains ("setTimeToLive"))) ;
  }
}
