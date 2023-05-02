
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_ProviderTest {
  @Test
  void getVersion() throws Exception {
    assertThat(synthesiseGPT("public class MyProvider extends Provider {\n    public MyProvider() {\n        super(\"MyProvider\", 1.0, \"MyProvider description\");\n    }\n\n    public double getVersion() {\n        return super.getVersion();\n    }\n}\n\n", "public class MyProvider extends Provider {\n    public MyProvider() {\n        super(\"MyProvider\", 1.0, \"MyProvider description\");\n    }\n\n    public double getProviderVersion() {\n        return Double.parseDouble(super.getProperty(\"Provider version\"));\n    }\n}\n", "java.security.Provider", "getVersion"), anyOf(contains("getVersionStr")));
  }
}