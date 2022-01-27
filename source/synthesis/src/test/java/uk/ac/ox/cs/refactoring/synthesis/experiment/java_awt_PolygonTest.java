package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_PolygonTest {

  @Test
  void getBoundingBox() throws Exception {
    assertThat(synthesiseAlias("java.awt.Polygon", "getBoundingBox"), contains(".getBounds("));
  }

  @Test
  void inside() throws Exception {
    assertThat(synthesiseAlias("java.awt.Polygon", "inside", "int", "int"), contains(".contains("));
  }
}
