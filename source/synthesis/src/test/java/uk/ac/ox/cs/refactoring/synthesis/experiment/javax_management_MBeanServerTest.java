
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_management_MBeanServerTest {
  @Test
  void deserialize1() throws Exception {
    assertThat(synthesiseAlias("javax.management.MBeanServer", "deserialize", "java.lang.String", "byte[]"), anyOf(contains("getClassLoaderRepository")));
  }

  @Test
  void deserialize2() throws Exception {
    assertThat(synthesiseAlias("javax.management.MBeanServer", "deserialize", "java.lang.String", "javax.management.ObjectName", "byte[]"), anyOf(contains("getClassLoader")));
  }

  @Test
  void deserialize3() throws Exception {
    assertThat(synthesiseAlias("javax.management.MBeanServer", "deserialize", "javax.management.ObjectName", "byte[]"), anyOf(contains("getClassLoaderFor")));
  }
}