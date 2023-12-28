
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
assertThat (synthesiseGPT ("defineClass" , "this.defineClass(param0, param1, param2);" , "" , "java.lang.ClassLoader" , "defineClass" , "byte[]" , "int" , "int") , anyOf (contains ("defineClass"))) ;
  }

  @Test
  void getPackage() throws Exception {
assertThat (synthesiseGPT ("getPackage" , "this.getPackage(param0);" , "\nthis.getClassLoader().getDefinedPackage(param0);\n" , "java.lang.ClassLoader" , "getPackage" , "java.lang.String") , anyOf (contains ("Package") , contains ("class") , contains ("getDefinedPackage") , contains ("getDefinedPackage"))) ;
  }
}
