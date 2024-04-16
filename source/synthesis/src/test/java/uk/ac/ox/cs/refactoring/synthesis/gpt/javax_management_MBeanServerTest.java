
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_MBeanServerTest {
  @Test
  void deserialize1() throws Exception {
assertThat (synthesiseNeural ("deserialize1" , "this.deserialize(param0, param1);" , "\nClassLoaderRepository clr = this.getClassLoaderRepository();\nObject obj = clr.loadClass(param0, param1);\n" , "javax.management.MBeanServer" , "deserialize" , "java.lang.String" , "byte[]") , anyOf (contains ("getClassLoaderRepository"))) ;
  }

  @Test
  void deserialize2() throws Exception {
assertThat (synthesiseNeural ("deserialize2" , "this.deserialize(param0, param1, param2);" , "\nClassLoader classLoader = getClassLoader();\ntry (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(param2))) {\n    Object obj = in.readObject();\n    this.registerMBean(obj, param1);\n} catch (IOException | ClassNotFoundException e) {\n    throw new RuntimeException(e);\n}\n" , "javax.management.MBeanServer" , "deserialize" , "java.lang.String" , "javax.management.ObjectName" , "byte[]") , anyOf (contains ("getClassLoader"))) ;
  }

  @Test
  void deserialize3() throws Exception {
assertThat (synthesiseNeural ("deserialize3" , "this.deserialize(param0, param1);" , "\nClassLoader cl = this.getClassLoaderFor(param0);\ntry (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(param1))) {\n    return ois.readObject();\n} catch (IOException | ClassNotFoundException e) {\n    throw new RuntimeException(e);\n}\n" , "javax.management.MBeanServer" , "deserialize" , "javax.management.ObjectName" , "byte[]") , anyOf (contains ("getClassLoaderFor"))) ;
  }
}
