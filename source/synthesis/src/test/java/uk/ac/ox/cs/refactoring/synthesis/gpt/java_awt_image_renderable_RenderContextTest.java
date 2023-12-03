
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_image_renderable_RenderContextTest {
  @Test
  void concetenateTransform() throws Exception {
assertThat (synthesiseGPT ("this.concetenateTransform(param0);" , "\nthis.preConcatenateTransform(param0);\n;" , "java.awt.image.renderable.RenderContext" , "concetenateTransform" , "java.awt.geom.AffineTransform") , anyOf (contains ("concatenateTransform"))) ;
  }

  @Test
  void preConcetenateTransform() throws Exception {
assertThat (synthesiseGPT ("this.preConcetenateTransform(param0);" , "\nthis.concatenate(param0)\n;" , "java.awt.image.renderable.RenderContext" , "preConcetenateTransform" , "java.awt.geom.AffineTransform") , anyOf (contains ("preConcatenateTransform"))) ;
  }
}
