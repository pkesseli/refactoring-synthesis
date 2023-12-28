
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_SecurityTest {
  @Test
  void getAlgorithmProperty() throws Exception {
assertThat (synthesiseGPT ("getAlgorithmProperty" , "this.getAlgorithmProperty(param0, param1);" , "\nfinal Provider provider = Security.getProvider(\"SUN\");\nif (provider != null) {\n    final String value = provider.getProperty(\"Alg.Alias.\" + param0 + \".\" + param1);\n    if (value != null) {\n        return provider.getProperty(value);\n    }\n}\nreturn null;\n" , "java.security.Security" , "getAlgorithmProperty" , "java.lang.String" , "java.lang.String") , anyOf (contains ("AlgorithmParameters") , contains ("KeyFactory"))) ;
  }
}
