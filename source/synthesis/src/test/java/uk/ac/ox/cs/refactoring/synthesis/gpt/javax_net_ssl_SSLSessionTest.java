
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_net_ssl_SSLSessionTest {
  @Test
  void getPeerCertificateChain() throws Exception {
    assertThat(synthesiseGPT("javax.net.ssl.SSLSession session = // get SSLSession object\njavax.security.cert.X509Certificate[] certs = session.getPeerCertificateChain();\n\n", "javax.net.ssl.SSLSession session = // get SSLSession object\njava.security.cert.Certificate[] certs = session.getPeerCertificates();\n", "javax.net.ssl.SSLSession", "getPeerCertificateChain"), anyOf(contains("Certificate"), contains("getPeerCertificates")));
  }
}