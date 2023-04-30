
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_registry_RegistryHandlerTest {
  @Test
  void registryImpl() throws Exception {
    assertThat(synthesiseGPT("Registry registry = this.registryImpl(a);\n\n", "Registry registry = LocateRegistry.getRegistry(a);\n", "java.rmi.registry.RegistryHandler", "registryImpl", "int"), anyOf(contains("RegistryHandler")));
  }

  @Test
  void registryStub() throws Exception {
    assertThat(synthesiseGPT("RegistryHandler registryHandler = new RegistryHandler();\nRegistry registry = (Registry) registryHandler.registryStub(\"localhost\", 1099);\n\n", "Registry registry = LocateRegistry.getRegistry(\"localhost\", 1099);\n", "java.rmi.registry.RegistryHandler", "registryStub", "java.lang.String", "int"), anyOf(contains("RegistryHandler")));
  }
}