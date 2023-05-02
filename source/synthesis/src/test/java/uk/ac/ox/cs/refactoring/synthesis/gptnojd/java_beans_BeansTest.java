
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_beans_BeansTest {
  @Test
  void instantiate() throws Exception {
    assertThat(synthesiseGPT("this.instantiate(a, b, c, d);\n\n", "java.beans.XMLDecoder decoder = new java.beans.XMLDecoder(new java.io.ByteArrayInputStream(b.getBytes()));\ndecoder.setClassLoader(a);\ndecoder.setExceptionListener(e -> System.out.println(\"Exception: \" + e.toString()));\nObject obj = decoder.readObject();\ndecoder.close();\n", "java.beans.Beans", "instantiate", "java.lang.ClassLoader", "java.lang.String", " java.beans.beancontext.BeanContext", "java.beans.AppletInitializer"), anyOf(contains("instantiate")));
  }
}