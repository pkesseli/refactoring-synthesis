package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class java_net_MulticastSocketTest {

  @Test
  @Disabled("We do not recognise generic types, and thus would conclude return types differ.")
  void getLoopbackMode() {
  }

  @Test
  void getTTL() throws Exception {
    assertThat(synthesiseAlias("java.net.MulticastSocket", "getTTL"), contains(".getTimeToLive("));
  }

  @Test
  @Disabled("Argument types differ.")
  void joinGroup() {
  }

  @Test
  @Disabled("Argument types differ.")
  void leaveGroup() {
  }

  @Test
  @Disabled("We do not recognise generic types, and thus would conclude argument types differ.")
  void setLoopbackMode() {
  }

  @Test
  void setTTL() throws Exception {
    assertThat(synthesiseAlias("java.net.MulticastSocket", "setTTL", "byte"), contains(".setTimeToLive("));
  }
}
