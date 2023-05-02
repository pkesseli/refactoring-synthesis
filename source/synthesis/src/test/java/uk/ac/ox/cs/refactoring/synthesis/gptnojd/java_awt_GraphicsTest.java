
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_GraphicsTest {
  @Test
  void getClipRect() throws Exception {
    assertThat(synthesiseGPT("Rectangle clipRect = this.getClipRect();\n\n", "Shape clipShape = this.getClip();\nRectangle clipRect = clipShape.getBounds();\n", "java.awt.Graphics", "getClipRect"), anyOf(contains("getClipBounds")));
  }
}