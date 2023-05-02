
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_LabelViewTest {
  @Test
  void getFontMetrics() throws Exception {
    assertThat(synthesiseGPT("FontMetrics fontMetrics = this.getFontMetrics();\n\n", "FontMetrics fontMetrics = this.getFont().getFontMetrics(this.getFontRenderContext());\n", "javax.swing.text.LabelView", "getFontMetrics"), Matchers.anything());
  }
}