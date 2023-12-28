
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
assertThat (synthesiseGPT ("checkMulticast" , "this.checkMulticast(param0, param1);" , "\nfinal SecurityManager securityManager = System.getSecurityManager();\nif (securityManager != null) {\n    securityManager.checkPermission(new SocketPermission(param0.getHostAddress(), \"accept, connect, listen, multicast\"));\n}\n" , "java.lang.SecurityManager" , "checkMulticast" , "java.net.InetAddress" , "byte") , anyOf (contains ("checkPermission"))) ;
  }
}
