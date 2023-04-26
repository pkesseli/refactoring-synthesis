
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_net_ssl_HandshakeCompletedEventTest {
  @Test
  void getPeerCertificateChain() throws Exception {
    assertThat(synthesiseAlias("javax.net.ssl.HandshakeCompletedEvent", "getPeerCertificateChain"), contains("getPeerCertificates"));
  }
}