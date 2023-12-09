
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_ScrollPaneLayoutTest {
  @Test
  void getViewportBorderBounds() throws Exception {
assertThat (synthesiseGPT ("this.getViewportBorderBounds(param0);" , "" , "javax.swing.ScrollPaneLayout" , "getViewportBorderBounds" , "javax.swing.JScrollPane") , anyOf (contains ("getViewportBorderBounds"))) ;
  }
}
