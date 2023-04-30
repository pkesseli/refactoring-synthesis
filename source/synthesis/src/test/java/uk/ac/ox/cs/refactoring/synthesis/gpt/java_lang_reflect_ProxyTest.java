
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_reflect_ProxyTest {
  @Test
  void getProxyClass() throws Exception {
    assertThat(synthesiseGPT("Class<?> proxyClass = this.getProxyClass(a, b);\n\n", "Class<?> proxyClass = Proxy.newProxyInstance(a, b, null).getClass();\n", "java.lang.reflect.Proxy", "getProxyClass", "java.lang.ClassLoader", "Class&lt;?&gt;..."), anyOf(contains("IllegalAccessException"), contains("newInstance"), contains("newProxyInstance")));
  }
}