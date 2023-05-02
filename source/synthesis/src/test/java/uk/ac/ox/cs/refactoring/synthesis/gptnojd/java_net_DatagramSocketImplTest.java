
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_DatagramSocketImplTest {
  @Test
  void getTTL() throws Exception {
    assertThat(synthesiseGPT("int ttl = this.getTTL();\n\n", "DatagramSocket socket = new DatagramSocket();\nsocket.getOption(IP_MULTICAST_TTL);\n", "java.net.DatagramSocketImpl", "getTTL"), Matchers.anything());
  }

  @Test
  void setTTL() throws Exception {
    assertThat(synthesiseGPT("this.setTTL(a);\n", "DatagramSocket socket = new DatagramSocket();\nsocket.setTimeToLive(a);\n", "java.net.DatagramSocketImpl", "setTTL", "byte"), Matchers.anything());
  }
}