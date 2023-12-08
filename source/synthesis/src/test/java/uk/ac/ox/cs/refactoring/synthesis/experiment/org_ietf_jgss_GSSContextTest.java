
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class org_ietf_jgss_GSSContextTest {
  @Test
  void acceptSecContext() throws Exception {
    // org.ietf.jgss.GSSContext
    assertThat(synthesiseAlias("sun.security.jgss.GSSContextImpl", "acceptSecContext", "java.io.InputStream", "java.io.OutputStream"), anyOf(contains("acceptSecContext")));
  }

  @Test
  void getMIC() throws Exception {
    assertThat(synthesiseAlias("org.ietf.jgss.GSSContext", "getMIC", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("getMIC")));
  }

  @Test
  void initSecContext() throws Exception {
    // org.ietf.jgss.GSSContext
    assertThat(synthesiseAlias("sun.security.jgss.GSSContextImpl", "initSecContext", "java.io.InputStream", "java.io.OutputStream"), anyOf(contains("initSecContext")));
  }

  @Test
  void unwrap() throws Exception {
    assertThat(synthesiseAlias("org.ietf.jgss.GSSContext", "unwrap", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("unwrap")));
  }

  @Test
  void verifyMIC() throws Exception {
    assertThat(synthesiseAlias("org.ietf.jgss.GSSContext", "verifyMIC", "java.io.InputStream", "java.io.InputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("verifyMIC")));
  }

  @Test
  void wrap() throws Exception {
    assertThat(synthesiseAlias("org.ietf.jgss.GSSContext", "wrap", "java.io.InputStream", "java.io.OutputStream", "org.ietf.jgss.MessageProp"), anyOf(contains("wrap")));
  }
}