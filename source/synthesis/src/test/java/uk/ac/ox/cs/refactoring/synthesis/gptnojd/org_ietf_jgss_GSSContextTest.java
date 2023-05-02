
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class org_ietf_jgss_GSSContextTest {
  @Test
  void acceptSecContext() throws Exception {
    assertThat(synthesiseGPT("this.acceptSecContext(a, b);\n", "this.acceptSecContext();\n", "org.ietf.jgss.GSSContext", "acceptSecContext", "java.io.InputStream", "java.io.OutputStream"), anyOf(contains("acceptSecContext")));
  }

  @Test
  void getMIC() throws Exception {
    assertThat(synthesiseGPT("this.getMIC(a, b, c);\n\n", "this.getMIC(a, 0, a.length, b, 0, b.length, c);\n", "org.ietf.jgss.GSSContext", "getMIC", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("getMIC")));
  }

  @Test
  void initSecContext() throws Exception {
    assertThat(synthesiseGPT("this.initSecContext(a, b);\n", "GSSContext context = GSSManager.getInstance().createContext(null, null, null, GSSContext.DEFAULT_LIFETIME);\nbyte[] token = context.initSecContext(new byte[0], 0, 0);\nOutputStream out = new OutputStream();\nout.write(token);\nout.flush();\n", "org.ietf.jgss.GSSContext", "initSecContext", "java.io.InputStream", "java.io.OutputStream"), anyOf(contains("initSecContext")));
  }

  @Test
  void unwrap() throws Exception {
    assertThat(synthesiseGPT("this.unwrap(a, b, c);\n", "this.unwrap(a.readAllBytes(), 0, a.available(), b, 0, c);\n", "org.ietf.jgss.GSSContext", "unwrap", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("unwrap")));
  }

  @Test
  void verifyMIC() throws Exception {
    assertThat(synthesiseGPT("this.verifyMIC(a, b, c);\n", "MessageProp prop = new MessageProp(0, false);\nbyte[] token = new byte[a.available()];\na.read(token);\nthis.verifyMIC(token, 0, token.length, b, 0, b.length, prop);\n", "org.ietf.jgss.GSSContext", "verifyMIC", "java.io.InputStream", "java.io.InputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("verifyMIC")));
  }

  @Test
  void wrap() throws Exception {
    assertThat(synthesiseGPT("this.wrap(a, b, c);\n\n", "OutputStream outputStream = this.wrap(b, c);\nIOUtils.copy(a, outputStream);\noutputStream.flush();\n", "org.ietf.jgss.GSSContext", "wrap", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("wrap")));
  }
}