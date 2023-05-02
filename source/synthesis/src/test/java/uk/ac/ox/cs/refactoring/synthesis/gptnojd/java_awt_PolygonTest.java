
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_PolygonTest {
  @Test
  void getBoundingBox() throws Exception {
    assertThat(synthesiseGPT("import java.awt.Polygon;\n\npublic class MyPolygon extends Polygon {\n    public Rectangle getBoundingBox() {\n        return super.getBoundingBox();\n    }\n}\n\n", "import java.awt.Polygon;\nimport java.awt.Rectangle;\n\npublic class MyPolygon extends Polygon {\n    public Rectangle getBoundingBox() {\n        return super.getBounds();\n    }\n}\n", "java.awt.Polygon", "getBoundingBox"), anyOf(contains("getBounds")));
  }

  @Test
  void inside() throws Exception {
    assertThat(synthesiseGPT("if (this.inside(a, b)) {\n    // do something\n}\n\n", "if (new java.awt.Polygon(xPoints, yPoints, nPoints).contains(a, b)) {\n    // do something\n}\n", "java.awt.Polygon", "inside", "int", "int"), anyOf(contains("contains")));
  }
}