
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ClassLoaderTest {
  @Test
  void defineClass() throws Exception {
    assertThat(synthesiseGPT("public class MyClassLoader extends ClassLoader {\n    public Class<?> loadClass(String name, byte[] bytes) {\n        return defineClass(name, bytes, 0, bytes.length);\n    }\n}\n\n", "public class MyClassLoader extends ClassLoader {\n    public Class<?> loadClass(String name, byte[] bytes) {\n        return defineClass(name, bytes, 0, bytes.length, null);\n    }\n}\n", "java.lang.ClassLoader", "defineClass", "byte[]", "int", "int"), anyOf(contains("defineClass")));
  }

  @Test
  void getPackage() throws Exception {
    assertThat(synthesiseGPT("Package p = this.getPackage(a);\n\n", "Package p = Package.getPackage(a);\n", "java.lang.ClassLoader", "getPackage", "java.lang.String"), anyOf(contains("Package"), contains("class"), contains("getDefinedPackage"), contains("getPackage")));
  }
}