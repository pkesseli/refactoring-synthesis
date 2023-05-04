
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_lang_PackageTest {

  @Test
  @Disabled("Not in our minimal grammar. Inlining would provide a solution, but not a desirable one. A proper refactoring would restructure the code to make a relevant class loader avaialble.")
  void getPackage() throws Exception {
    assertThat(synthesiseAlias("java.lang.Package", "getPackage", "java.lang.String"), contains(".getDefinedPackage("));
  }
}
