package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_awt_image_renderable_RenderContextTest {

  @Test
  void concetenateTransform() throws Exception {
    assertThat(synthesiseAlias("java.awt.image.renderable.RenderContext", "concetenateTransform",
        "java.awt.geom.AffineTransform"), contains(".concatenateTransform("));
  }

  @Test
  void preConcetenateTransform() throws Exception {
    assertThat(synthesiseAlias("java.awt.image.renderable.RenderContext", "preConcetenateTransform",
        "java.awt.geom.AffineTransform"), contains(".preConcatenateTransform("));
  }
}
