
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_net_DatagramSocketImplTest {
  @Test
  void getTTL() throws Exception {
    // java.net.DatagramSocketImpl
    assertThat(synthesiseAlias("java.net.PlainDatagramSocketImpl", "getTTL"), contains("getTimeToLive"));
  }

  @Test
  void setTTL() throws Exception {
    assertThat(synthesiseAlias("java.net.DatagramSocketImpl", "setTTL", "byte"), contains("setTimeToLive"));
  }
}