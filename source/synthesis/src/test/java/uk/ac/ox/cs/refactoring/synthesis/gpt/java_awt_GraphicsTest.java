
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_GraphicsTest {
  @Test
  void finalize1() throws Exception {
    assertThat(synthesiseGPT("public void cleanup() {\n    // perform cleanup actions\n    this.finalize();\n}\n\n", "public void cleanup() {\n    // perform cleanup actions\n    // alternative cleanup mechanism\n}\n", "java.awt.Graphics", "finalize"), anyOf(contains("finalize")));
  }

  @Test
  void getClipRect() throws Exception {
    assertThat(synthesiseGPT("Rectangle clipRect = this.getClipRect();\n\n", "Rectangle clipBounds = this.getClipBounds();\nRectangle clipRect = new Rectangle(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);\n", "java.awt.Graphics", "getClipRect"), anyOf(contains("getClipBounds")));
  }
}