
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
assertThat (synthesiseGPT ("getPeerCertificateChain" , "this.getPeerCertificateChain();" , "\nfinal Certificate[] peerCertificates = this.getPeerCertificates();\nif (peerCertificates != null && peerCertificates.length > 0 && peerCertificates[0] instanceof X509Certificate) {\n    final X509Certificate[] peerCertificateChain = new X509Certificate[peerCertificates.length];\n    for (int i = 0; i < peerCertificates.length; i++) {\n        peerCertificateChain[i] = (X509Certificate) peerCertificates[i];\n    }\n    return peerCertificateChain;\n} else {\n    return null;\n}\n" , "javax.net.ssl.SSLSession" , "getPeerCertificateChain") , anyOf (contains ("Certificate") , contains ("getPeerCertificates"))) ;
  }
}
