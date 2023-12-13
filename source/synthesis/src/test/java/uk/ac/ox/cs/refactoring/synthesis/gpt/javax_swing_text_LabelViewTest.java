
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_LabelViewTest {
  @Test
  void getFontMetrics() throws Exception {
assertThat (synthesiseGPT ("getFontMetrics" , "this.getFontMetrics();" , "\nGraphics g = this.getGraphics();\nFontMetrics fm = g.getFontMetrics(this.getFont());\n" , "javax.swing.text.LabelView" , "getFontMetrics") , Matchers . anything ()) ;
  }
}
