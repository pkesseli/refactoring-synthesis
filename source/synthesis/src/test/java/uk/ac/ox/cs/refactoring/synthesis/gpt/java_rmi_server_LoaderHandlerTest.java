
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_LoaderHandlerTest {
  @Test
  void getSecurityContext() throws Exception {
    assertThat(synthesiseGPT("SecurityContext context = LoaderHandler.getSecurityContext(a);\n\n", "SecurityManager sm = System.getSecurityManager();\nSecurityContext context = (sm != null) ? sm.getSecurityContext() : null;\n", "java.rmi.server.LoaderHandler", "getSecurityContext", "java.lang.ClassLoader"), Matchers.anything());
  }

  @Test
  void loadClass1() throws Exception {
    assertThat(synthesiseGPT("this.loadClass(a);\n\n", "Class.forName(a);\n", "java.rmi.server.LoaderHandler", "loadClass", "java.lang.String"), Matchers.anything());
  }

  @Test
  void loadClass2() throws Exception {
    assertThat(synthesiseGPT("this.loadClass(a, b);\n\n", "URLClassLoader.newInstance(new URL[]{a}).loadClass(b);\n", "java.rmi.server.LoaderHandler", "loadClass", "java.net.URL", "java.lang.String"), Matchers.anything());
  }
}