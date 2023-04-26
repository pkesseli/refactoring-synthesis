
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_beans_BeansTest {
  @Test
  void instantiate() throws Exception {
    assertThat(synthesiseAlias("java.beans.Beans", "instantiate", "java.lang.ClassLoader", "java.lang.String", "java.beans.beancontext.BeanContext", "java.beans.AppletInitializer"), anyOf(contains("instantiate")));
  }
}