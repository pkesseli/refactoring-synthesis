
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_net_ssl_HandshakeCompletedEventTest {
  @Test
  void getPeerCertificateChain() throws Exception {
    assertThat(synthesiseGPT("javax.net.ssl.SSLSession session = event.getSession();\njavax.security.cert.X509Certificate[] certs = session.getPeerCertificateChain();\n\n", "javax.net.ssl.SSLSession session = event.getSession();\njava.security.cert.Certificate[] certs = session.getPeerCertificates();\n", "javax.net.ssl.HandshakeCompletedEvent", "getPeerCertificateChain"), anyOf(contains("Certificate"), contains("getPeerCertificates")));
  }
}