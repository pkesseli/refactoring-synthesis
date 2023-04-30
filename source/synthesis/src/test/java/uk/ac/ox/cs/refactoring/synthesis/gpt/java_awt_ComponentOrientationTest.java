
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ComponentOrientationTest {
  @Test
  void getOrientation() throws Exception {
    assertThat(synthesiseGPT("ComponentOrientation orientation = this.getOrientation(a);\n\n", "ComponentOrientation orientation = ComponentOrientation.getOrientation(a.getLocale());\n", "java.awt.ComponentOrientation", "getOrientation", "java.util.ResourceBundle"), anyOf(contains("getOrientation")));
  }
}