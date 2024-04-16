
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_SecurityManagerTest {
  @Test
  void checkMulticast() throws Exception {
assertThat (synthesiseNeural ("checkMulticast" , "this.checkMulticast(param0, param1);" , "\nthis.checkPermission(new java.net.SocketPermission(param0.getHostAddress(), \"accept, connect, listen, resolve\"));\n" , "java.lang.SecurityManager" , "checkMulticast" , "java.net.InetAddress" , "byte") , anyOf (contains ("checkPermission"))) ;
  }
}
