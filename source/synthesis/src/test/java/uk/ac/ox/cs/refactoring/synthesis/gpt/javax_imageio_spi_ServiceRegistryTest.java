
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_imageio_spi_ServiceRegistryTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public class MyClass {\n    // ...\n    public void cleanup() {\n        // ...\n        try {\n            ServiceRegistry registry = // ...\n            registry.finalize();\n        } catch (Throwable t) {\n            // ...\n        }\n    }\n    // ...\n}\n", "public class MyClass {\n    // ...\n    public void cleanup() {\n        // ...\n        try {\n            ServiceRegistry registry = // ...\n            registry.dispose();\n        } catch (Throwable t) {\n            // ...\n        }\n    }\n    // ...\n}\n", "javax.imageio.spi.ServiceRegistry", "finalize"), anyOf(contains("finalize")));
  }
}