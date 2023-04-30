
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RemoteCallTest {
  @Test
  void done() throws Exception {
    assertThat(synthesiseGPT("this.done();\n\n", "", "java.rmi.server.RemoteCall", "done"), Matchers.anything());
  }

  @Test
  void executeCall() throws Exception {
    assertThat(synthesiseGPT("this.executeCall();\n\n", "throw new UnsupportedOperationException(\"executeCall() is deprecated and has no replacement\");\n", "java.rmi.server.RemoteCall", "executeCall"), Matchers.anything());
  }

  @Test
  void getInputStream() throws Exception {
    assertThat(synthesiseGPT("InputStream inputStream = this.getInputStream();\n\n", "throw new UnsupportedOperationException(\"This method is deprecated and has no replacement.\");\n", "java.rmi.server.RemoteCall", "getInputStream"), Matchers.anything());
  }

  @Test
  void getOutputStream() throws Exception {
    assertThat(synthesiseGPT("OutputStream outputStream = this.getOutputStream();\n\n", "throw new UnsupportedOperationException(\"This method is deprecated and has no replacement.\");\n", "java.rmi.server.RemoteCall", "getOutputStream"), Matchers.anything());
  }

  @Test
  void getResultStream() throws Exception {
    assertThat(synthesiseGPT("InputStream resultStream = this.getResultStream(a);\n\n", "throw new UnsupportedOperationException(\"This method is deprecated and has no replacement.\");\n", "java.rmi.server.RemoteCall", "getResultStream", "boolean"), Matchers.anything());
  }

  @Test
  void releaseInputStream() throws Exception {
    assertThat(synthesiseGPT("this.releaseInputStream();\n\n", "", "java.rmi.server.RemoteCall", "releaseInputStream"), Matchers.anything());
  }

  @Test
  void releaseOutputStream() throws Exception {
    assertThat(synthesiseGPT("this.releaseOutputStream();\n\n", "", "java.rmi.server.RemoteCall", "releaseOutputStream"), Matchers.anything());
  }
}