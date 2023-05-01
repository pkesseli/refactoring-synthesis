
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_MBeanServerTest {
  @Test
  void deserialize1() throws Exception {
    assertThat(synthesiseGPT("this.deserialize(a, b);\n", "ClassLoaderRepository clr = MBeanServerFactory.getClassLoaderRepository();\nObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(b));\nObject obj = ois.readObject();\nois.close();\nthis.deserialize(a, obj, clr);\n", "javax.management.MBeanServer", "deserialize", "java.lang.String", "byte[]"), anyOf(contains("getClassLoaderRepository")));
  }

  @Test
  void deserialize2() throws Exception {
    assertThat(synthesiseGPT("this.deserialize(a, b, c);\n", "ClassLoader classLoader = Thread.currentThread().getContextClassLoader();\nObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(c));\nObject obj = in.readObject();\nin.close();\nthis.getMBeanServer().registerMBean(obj, b);\n", "javax.management.MBeanServer", "deserialize", "java.lang.String", "javax.management.ObjectName", "byte[]"), anyOf(contains("getClassLoader")));
  }

  @Test
  void deserialize3() throws Exception {
    assertThat(synthesiseGPT("this.deserialize(a, b);\n", "ClassLoader cl = getClassLoaderFor(a);\nObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));\nObject obj = in.readObject();\nin.close();\n", "javax.management.MBeanServer", "deserialize", "javax.management.ObjectName", "byte[]"), anyOf(contains("getClassLoaderFor")));
  }
}