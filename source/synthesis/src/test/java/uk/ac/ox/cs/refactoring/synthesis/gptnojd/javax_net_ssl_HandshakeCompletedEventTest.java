
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_net_ssl_HandshakeCompletedEventTest {
  @Test
  void getPeerCertificateChain() throws Exception {
    assertThat(synthesiseGPT("javax.net.ssl.SSLSession session = sslSocket.getSession();\nX509Certificate[] peerCertificates = session.getPeerCertificateChain();\n\n", "javax.net.ssl.SSLSession session = sslSocket.getSession();\nX509Certificate[] peerCertificates = null;\nif (session != null) {\n    peerCertificates = session.getPeerCertificates();\n}\n", "javax.net.ssl.HandshakeCompletedEvent", "getPeerCertificateChain"), anyOf(contains("Certificate"), contains("getPeerCertificates")));
  }
}