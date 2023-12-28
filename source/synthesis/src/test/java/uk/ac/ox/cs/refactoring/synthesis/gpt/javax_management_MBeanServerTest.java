
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
assertThat (synthesiseGPT ("deserialize1" , "this.deserialize(param0, param1);" , "\nfinal MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);\nfinal ClassLoaderRepository clr = server.getClassLoaderRepository();\nfinal ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(param1));\nois.setClassLoader(clr);\nois.readObject();\n" , "javax.management.MBeanServer" , "deserialize" , "java.lang.String" , "byte[]") , anyOf (contains ("getClassLoaderRepository"))) ;
  }

  @Test
  void deserialize2() throws Exception {
assertThat (synthesiseGPT ("deserialize2" , "this.deserialize(param0, param1, param2);" , "\nfinal ClassLoader classLoader = Thread.currentThread().getContextClassLoader();\nfinal ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(param2);\nfinal ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream) {\n    @Override\n    protected Class<?> resolveClass(final ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {\n        return Class.forName(objectStreamClass.getName(), false, classLoader);\n    }\n};\nobjectInputStream.readObject();\n" , "javax.management.MBeanServer" , "deserialize" , "java.lang.String" , "javax.management.ObjectName" , "byte[]") , anyOf (contains ("getClassLoader"))) ;
  }

  @Test
  void deserialize3() throws Exception {
assertThat (synthesiseGPT ("deserialize3" , "this.deserialize(param0, param1);" , "\nfinal ClassLoader classLoader = getClassLoaderFor(param0);\nfinal ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(param1);\nfinal ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);\nfinal Object object = objectInputStream.readObject();\nobjectInputStream.close();\nreturn object;\n" , "javax.management.MBeanServer" , "deserialize" , "javax.management.ObjectName" , "byte[]") , anyOf (contains ("getClassLoaderFor"))) ;
  }
}
