
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_SecurityManagerTest {
  @Test
  void checkMulticast() throws Exception {
    assertThat(synthesiseGPT("this.checkMulticast(a, b);\n", "SecurityManager securityManager = System.getSecurityManager();\nif (securityManager != null) {\n    securityManager.checkPermission(new java.net.SocketPermission(a.getHostAddress(), \"accept,connect,listen,resolve\"));\n}\n", "java.lang.SecurityManager", "checkMulticast", "java.net.InetAddress", "byte"), Matchers.anything());
  }
}