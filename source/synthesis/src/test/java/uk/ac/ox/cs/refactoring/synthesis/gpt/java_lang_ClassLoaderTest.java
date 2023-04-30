
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_ClassLoaderTest {
  @Test
  void defineClass() throws Exception {
    assertThat(synthesiseGPT("this.defineClass(a, b, c);\n\n", "this.defineClass(null, a, b, c);\n", "java.lang.ClassLoader", "defineClass", "byte[]", "int", "int"), anyOf(contains("defineClass")));
  }

  @Test
  void getPackage() throws Exception {
    assertThat(synthesiseGPT("Package p = this.getPackage(\"com.example.packageName\");\n\n", "ClassLoader cl = this.getClass().getClassLoader();\nPackage p = cl.getDefinedPackage(\"com.example.packageName\");\n", "java.lang.ClassLoader", "getPackage", "java.lang.String"), anyOf(contains("Package"), contains("class"), contains("getDefinedPackage"), contains("getPackage")));
  }
}