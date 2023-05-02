
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_MenuItemTest {
  @Test
  void disable() throws Exception {
    assertThat(synthesiseGPT("this.disable();\n\n", "this.setEnabled(false);\n", "java.awt.MenuItem", "disable"), anyOf(contains("setEnabled")));
  }

  @Test
  void enable1() throws Exception {
    assertThat(synthesiseGPT("this.enable();\n\n", "this.setEnabled(true);\n", "java.awt.MenuItem", "enable"), anyOf(contains("setEnabled")));
  }

  @Test
  void enable2() throws Exception {
    assertThat(synthesiseGPT("this.enable(a);\n\n", "this.setEnabled(a);\n", "java.awt.MenuItem", "enable", "boolean"), anyOf(contains("setEnabled")));
  }
}