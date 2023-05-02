
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_ScrollPaneLayoutTest {
  @Test
  void getViewportBorderBounds() throws Exception {
    assertThat(synthesiseGPT("Rectangle bounds = this.getViewportBorderBounds(a);\n\n", "Rectangle bounds = ((JComponent) a.getViewport().getBorder()).getBounds(a);\n", "javax.swing.ScrollPaneLayout", "getViewportBorderBounds", "javax.swing.JScrollPane"), anyOf(contains("getViewportBorderBounds")));
  }
}