
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_text_LabelViewTest {
  @Disabled("No replacement")
  @Test
  void getFontMetrics() throws Exception {
assertThat (synthesiseNeural ("getFontMetrics" , "this.getFontMetrics();" , "\nthis.getFont().getMetrics(this.getFont().getFontMetrics(this.getFont().getGraphics()));\n" , "javax.swing.text.LabelView" , "getFontMetrics") , Matchers . anything ()) ;
  }
}
