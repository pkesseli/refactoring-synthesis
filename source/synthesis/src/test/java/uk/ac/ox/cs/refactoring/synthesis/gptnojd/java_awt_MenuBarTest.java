
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_MenuBarTest {
  @Test
  void countMenus() throws Exception {
    assertThat(synthesiseGPT("int menuCount = this.countMenus();\n\n", "int menuCount = this.getMenuCount();\n", "java.awt.MenuBar", "countMenus"), anyOf(contains("getMenuCount")));
  }
}