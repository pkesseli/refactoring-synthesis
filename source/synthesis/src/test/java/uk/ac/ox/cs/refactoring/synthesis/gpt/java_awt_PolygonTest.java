
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.junit.jupiter.api.Test;

class java_awt_PolygonTest {
  @Test
  void getBoundingBox() throws Exception {
    assertThat(synthesiseGPT("Polygon polygon = new Polygon(new int[]{0, 5, 10}, new int[]{0, 10, 5}, 3);\nRectangle boundingBox = polygon.getBoundingBox();\n\n", "Polygon polygon = new Polygon(new int[]{0, 5, 10}, new int[]{0, 10, 5}, 3);\nRectangle boundingBox = polygon.getBounds();\n", "java.awt.Polygon", "getBoundingBox"), anyOf(contains("getBounds")));
  }

  @Test
  void inside() throws Exception {
    assertThat(synthesiseGPT("Polygon polygon = new Polygon(xPoints, yPoints, nPoints);\nboolean isInside = polygon.inside(x, y);\n\n", "Polygon polygon = new Polygon(xPoints, yPoints, nPoints);\nboolean isInside = polygon.contains(x, y);\n", "java.awt.Polygon", "inside", "int", "int"), anyOf(contains("contains")));
  }
}