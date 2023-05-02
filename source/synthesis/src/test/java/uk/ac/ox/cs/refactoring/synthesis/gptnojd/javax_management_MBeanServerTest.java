
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_MBeanServerTest {
  @Test
  void deserialize1() throws Exception {
    assertThat(synthesiseGPT("this.deserialize(a, b);\n\n", "ByteArrayInputStream in = new ByteArrayInputStream(b);\nObjectInputStream ois = new ObjectInputStream(in);\nthis.invoke(a, ois.readObject(), new String[0]);\n", "javax.management.MBeanServer", "deserialize", "java.lang.String", "byte[]"), anyOf(contains("getClassLoaderRepository")));
  }

  @Test
  void deserialize2() throws Exception {
    assertThat(synthesiseGPT("this.deserialize(a, b, c);\n\n", "ByteArrayInputStream bais = new ByteArrayInputStream(c);\nObjectInputStream ois = new ObjectInputStream(bais);\nObject obj = ois.readObject();\nthis.registerMBean(obj, b);\n", "javax.management.MBeanServer", "deserialize", "java.lang.String", "javax.management.ObjectName", "byte[]"), anyOf(contains("getClassLoader")));
  }

  @Test
  void deserialize3() throws Exception {
    assertThat(synthesiseGPT("this.deserialize(a, b);\n", "ByteArrayInputStream bis = new ByteArrayInputStream(b);\nObjectInputStream ois = new ObjectInputStream(bis);\nObject obj = ois.readObject();\nthis.registerMBean(obj, a);\n", "javax.management.MBeanServer", "deserialize", "javax.management.ObjectName", "byte[]"), anyOf(contains("getClassLoaderFor")));
  }
}