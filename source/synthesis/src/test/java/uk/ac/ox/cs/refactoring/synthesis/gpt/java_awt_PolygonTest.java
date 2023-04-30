
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_PolygonTest {
  @Test
  void getBoundingBox() throws Exception {
    assertThat(synthesiseGPT("this.getBoundingBox();\n\n", "this.getBounds();\n", "java.awt.Polygon", "getBoundingBox"), anyOf(contains("getBounds")));
  }

  @Test
  void inside() throws Exception {
    assertThat(synthesiseGPT("if (this.inside(a, b)) {\n    // do something\n}\n\n", "if (this.contains(a, b)) {\n    // do something\n}\n", "java.awt.Polygon", "inside", "int", "int"), anyOf(contains("contains")));
  }
}