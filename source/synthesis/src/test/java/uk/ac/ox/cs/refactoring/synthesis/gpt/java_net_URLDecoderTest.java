
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLDecoderTest {
  @Test
  void decode() throws Exception {
assertThat (synthesiseGPT ("decode" , "this.decode(param0);" , "\njava.net.URLDecoder.decode(param0, \"UTF-8\")\n;" , "java.net.URLDecoder" , "decode" , "java.lang.String") , Matchers . anything ()) ;
  }
}
