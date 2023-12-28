
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
assertThat (synthesiseGPT ("defineClass" , "this.defineClass(param0, param1, param2);" , "\nthis.defineClass(null, param1, param2, null);\n" , "java.lang.ClassLoader" , "defineClass" , "byte[]" , "int" , "int") , anyOf (contains ("defineClass"))) ;
  }

  @Test
  void getPackage() throws Exception {
assertThat (synthesiseGPT ("getPackage" , "this.getPackage(param0);" , "\nfinal Package definedPackage = this.getDefinedPackage(param0);\nif (definedPackage == null) {\n    return null;\n}\nreturn definedPackage.isSealed();\n" , "java.lang.ClassLoader" , "getPackage" , "java.lang.String") , anyOf (contains ("Package") , contains ("class") , contains ("getDefinedPackage") , contains ("getDefinedPackage"))) ;
  }
}
