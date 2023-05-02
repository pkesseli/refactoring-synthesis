
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_SignatureTest {
  @Test
  void getParameter() throws Exception {
    assertThat(synthesiseGPT("Signature signature = Signature.getInstance(\"SHA256withRSA\");\nString a = \"parameterName\";\nObject parameterValue = signature.getParameter(a);\n\n", "Signature signature = Signature.getInstance(\"SHA256withRSA\");\nString a = \"parameterName\";\nObject parameterValue = signature.getParameterInfo().getParameter(a);\n", "java.security.Signature", "getParameter", "java.lang.String"), Matchers.anything());
  }

  @Test
  void setParameter() throws Exception {
    assertThat(synthesiseGPT("this.setParameter(a, b);\n", "Signature signature = Signature.getInstance(\"SHA256withRSA\");\nsignature.setParameter(new SignatureParameterSpec(a, b));\n", "java.security.Signature", "setParameter", "java.lang.String", "java.lang.Object"), anyOf(contains("setParameter")));
  }
}