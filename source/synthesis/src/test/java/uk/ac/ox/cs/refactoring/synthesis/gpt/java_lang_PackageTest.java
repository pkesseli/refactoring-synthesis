
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_lang_PackageTest {
  @Test
  void getPackage() throws Exception {
    assertThat(synthesiseGPT("Package myPackage = this.getPackage(a);\n\n", "ClassLoader classLoader = this.getClass().getClassLoader();\nPackage myPackage = classLoader.getDefinedPackage(a);\n", "java.lang.Package", "getPackage", "java.lang.String"), anyOf(contains("Package"), contains("class"), contains("getDefinedPackage"), contains("getPackage")));
  }
}