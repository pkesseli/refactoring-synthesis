
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_SignatureSpiTest {
  @Test
  void engineGetParameter() throws Exception {
    assertThat(synthesiseGPT("public class MySignature extends SignatureSpi {\n    @Override\n    protected Object engineGetParameter(String a) throws InvalidParameterException {\n        // Some implementation here\n    }\n}\n\nMySignature mySig = new MySignature();\nObject param = mySig.engineGetParameter(\"paramName\");\n\n", "public class MySignature extends SignatureSpi {\n    @Override\n    protected <T extends AlgorithmParameterSpec> T engineGetParameter(Class<T> paramSpec) throws InvalidParameterException {\n        // Some implementation here\n    }\n}\n\nMySignature mySig = new MySignature();\nAlgorithmParameterSpec paramSpec = mySig.engineGetParameter(AlgorithmParameterSpec.class);\n", "java.security.SignatureSpi", "engineGetParameter", "java.lang.String"), Matchers.anything());
  }

  @Test
  void engineSetParameter() throws Exception {
    assertThat(synthesiseGPT("this.engineSetParameter(a, b);\n", "if (\"myParameter\".equals(a)) {\n    this.engineSetMyParameter(b);\n} else {\n    throw new UnsupportedOperationException(\"Unsupported parameter: \" + a);\n}\n", "java.security.SignatureSpi", "engineSetParameter", "java.lang.String", "java.lang.Object"), anyOf(contains("engineSetParameter")));
  }
}