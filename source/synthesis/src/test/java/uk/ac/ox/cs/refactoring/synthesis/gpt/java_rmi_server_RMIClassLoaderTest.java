
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RMIClassLoaderTest {
  @Test
  void getSecurityContext() throws Exception {
    assertThat(synthesiseGPT("SecurityContext context = this.getSecurityContext(a);\n\n", "SecurityManager securityManager = System.getSecurityManager();\nif (securityManager != null) {\n    securityManager.checkPermission(new RuntimePermission(\"getClassLoader\"));\n}\nSecurityContext context = AccessController.doPrivileged(\n    new PrivilegedAction<SecurityContext>() {\n        public SecurityContext run() {\n            return AccessController.getContext();\n        }\n    }\n);\n", "java.rmi.server.RMIClassLoader", "getSecurityContext", "java.lang.ClassLoader"), Matchers.anything());
  }

  @Test
  void loadClass() throws Exception {
    assertThat(synthesiseGPT("this.loadClass(a);\n\n", "this.loadClass(a, null);\n", "java.rmi.server.RMIClassLoader", "loadClass", "java.lang.String"), anyOf(contains("loadClass")));
  }
}