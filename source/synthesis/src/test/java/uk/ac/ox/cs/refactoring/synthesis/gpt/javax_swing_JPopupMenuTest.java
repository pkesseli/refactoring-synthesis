
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JPopupMenuTest {
  @Test
  void getComponentAtIndex() throws Exception {
    assertThat(synthesiseGPT("Component c = this.getComponentAtIndex(a);\n\n", "Component c = this.getComponent(a);\n", "javax.swing.JPopupMenu", "getComponentAtIndex", "int"), anyOf(contains("getComponent")));
  }
}