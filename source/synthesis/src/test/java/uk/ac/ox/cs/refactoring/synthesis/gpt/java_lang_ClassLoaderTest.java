
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
assertThat (synthesiseGPT ("this.defineClass(param0, param1, param2);" , "ClassLoader classLoader = getClass().getClassLoader();\nClass<?> clazz = classLoader.defineClass(null, param0, param1, param2);\nPackage definedPackage = classLoader.getDefinedPackage(clazz.getPackage().getName());" , "java.lang.ClassLoader" , "defineClass" , "byte[]" , "int" , "int") , anyOf (contains ("defineClass"))) ;
  }

  @Test
  void getPackage() throws Exception {
assertThat (synthesiseGPT ("this.getPackage(param0);" , "\nMethodHandles.Lookup lookup = MethodHandles.lookup();\nif (lookup.hasFullPrivilegeAccess()) {\n    this.getPackage(param0);\n} else {\n    // handle case where module access is not sufficient\n}\n;" , "java.lang.ClassLoader" , "getPackage" , "java.lang.String") , anyOf (contains ("Package") , contains ("class") , contains ("getDefinedPackage") , contains ("getPackage"))) ;
  }
}
