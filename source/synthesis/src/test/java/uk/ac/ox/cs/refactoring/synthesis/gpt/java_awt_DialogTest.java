
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_DialogTest {
  @Test
  void hide() throws Exception {
    assertThat(synthesiseGPT("this.hide();\n\n", "this.setVisible(false);\n", "java.awt.Dialog", "hide"), anyOf(contains("setVisible")));
  }

  @Test
  void show() throws Exception {
    assertThat(synthesiseGPT("this.show();\n\n", "this.setVisible(true);\n", "java.awt.Dialog", "show"), anyOf(contains("setVisible")));
  }
}