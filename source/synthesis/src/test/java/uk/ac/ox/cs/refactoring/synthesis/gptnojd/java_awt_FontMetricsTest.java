
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_FontMetricsTest {
  @Test
  void getMaxDecent() throws Exception {
    assertThat(synthesiseGPT("int descent = this.getMaxDecent();\n\n", "int descent = this.getFontMetrics(this.getFont()).getDescent();\n", "java.awt.FontMetrics", "getMaxDecent"), anyOf(contains("getMaxDescent")));
  }
}