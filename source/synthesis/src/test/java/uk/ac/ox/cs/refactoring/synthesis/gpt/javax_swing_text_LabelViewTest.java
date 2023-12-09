
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
assertThat (synthesiseGPT ("this.getFontMetrics();" , "\n// Assuming 'g' is a Graphics object obtained from a component's paint method or similar.\nFontMetrics fm = g.getFontMetrics(this.getFont());\n```\n\nIf you are within a `View` and you don't have direct access to a `Graphics` object, you might use the `getFontMetrics(Font)` method from the `Component` class, like so:\n\n```java\n// Assuming 'component' is a reference to the Component that contains this View.\nFontMetrics fm = component.getFontMetrics(this.getFont());\n;" , "javax.swing.text.LabelView" , "getFontMetrics") , Matchers . anything ()) ;
  }
}
