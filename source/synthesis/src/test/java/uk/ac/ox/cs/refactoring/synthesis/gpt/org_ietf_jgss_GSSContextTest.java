
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class org_ietf_jgss_GSSContextTest {
  @Test
  void acceptSecContext() throws Exception {
    assertThat(synthesiseGPT("this.acceptSecContext(a, b);\n\n", "this.acceptSecContext(a, 0, a.length);\n", "org.ietf.jgss.GSSContext", "acceptSecContext", "java.io.InputStream", "java.io.OutputStream"), anyOf(contains("acceptSecContext")));
  }

  @Test
  void getMIC() throws Exception {
    assertThat(synthesiseGPT("this.getMIC(a, b, c);\n", "byte[] mic = this.getMIC(a, 0, a.length, new MessageProp(0, true));\nb.write(mic);\n", "org.ietf.jgss.GSSContext", "getMIC", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("getMIC")));
  }

  @Test
  void initSecContext() throws Exception {
    assertThat(synthesiseGPT("this.initSecContext(a, b);\n\n", "byte[] token = this.initSecContext(a, 0, a.length);\nOutputStream out = ...;\nout.write(token);\n", "org.ietf.jgss.GSSContext", "initSecContext", "java.io.InputStream", "java.io.OutputStream"), anyOf(contains("initSecContext")));
  }

  @Test
  void unwrap() throws Exception {
    assertThat(synthesiseGPT("this.unwrap(a, b, c);\n\n", "byte[] bytes = new byte[a.available()];\na.read(bytes);\nthis.unwrap(bytes, 0, bytes.length, c);\n", "org.ietf.jgss.GSSContext", "unwrap", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("unwrap")));
  }

  @Test
  void verifyMIC() throws Exception {
    assertThat(synthesiseGPT("this.verifyMIC(a, b, c);\n\n", "byte[] aBytes = a.readAllBytes();\nbyte[] bBytes = b.readAllBytes();\nthis.verifyMIC(aBytes, 0, aBytes.length, bBytes, 0, bBytes.length, c);\n", "org.ietf.jgss.GSSContext", "verifyMIC", "java.io.InputStream", "java.io.InputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("verifyMIC")));
  }

  @Test
  void wrap() throws Exception {
    assertThat(synthesiseGPT("this.wrap(a, b, c);\n\n", "byte[] wrappedData = this.wrap(a, 0, a.length, c);\nb.write(wrappedData);\n", "org.ietf.jgss.GSSContext", "wrap", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("wrap")));
  }
}