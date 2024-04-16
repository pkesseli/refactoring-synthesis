
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_FontMetricsTest {
  @Test
  void getMaxDecent() throws Exception {
assertThat (synthesiseNeural ("getMaxDecent" , "this.getMaxDecent();" , "\nthis.getMaxDescent();\n" , "java.awt.FontMetrics" , "getMaxDecent") , anyOf (contains ("getMaxDescent"))) ;
  }
}
