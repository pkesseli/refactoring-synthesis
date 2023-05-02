
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_net_ssl_SSLSessionTest {
  @Test
  void getPeerCertificateChain() throws Exception {
    assertThat(synthesiseGPT("SSLSession session = sslSocket.getSession();\nX509Certificate[] peerCertificates = session.getPeerCertificateChain();\n\n", "SSLSession session = sslSocket.getSession();\nX509Certificate[] peerCertificates = null;\nif (session != null) {\n    Certificate[] certs = session.getPeerCertificates();\n    if (certs != null && certs.length > 0 && certs[0] instanceof X509Certificate) {\n        peerCertificates = Arrays.copyOf(certs, certs.length, X509Certificate[].class);\n    }\n}\n", "javax.net.ssl.SSLSession", "getPeerCertificateChain"), anyOf(contains("Certificate"), contains("getPeerCertificates")));
  }
}