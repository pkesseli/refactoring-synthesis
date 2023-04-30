
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_SignatureSpiTest {
  @Test
  void engineGetParameter() throws Exception {
    assertThat(synthesiseGPT("public byte[] sign(byte[] data) throws SignatureException {\n    AlgorithmParameters params = this.engineGetParameters();\n    byte[] a = params.getEncoded();\n    return this.engineSign(data);\n}\n", "public byte[] sign(byte[] data) throws SignatureException {\n    AlgorithmParameters params = this.engineGetParameters();\n    byte[] a = params.getEncoded(\"ASN.1\");\n    return this.engineSign(data);\n}\n", "java.security.SignatureSpi", "engineGetParameter", "java.lang.String"), Matchers.anything());
  }

  @Test
  void engineSetParameter() throws Exception {
    assertThat(synthesiseGPT("this.engineSetParameter(a, b);\n", "this.engineSetParameter(a, (java.lang.Object) b);\n", "java.security.SignatureSpi", "engineSetParameter", "java.lang.String", "java.lang.Object"), anyOf(contains("engineSetParameter")));
  }
}