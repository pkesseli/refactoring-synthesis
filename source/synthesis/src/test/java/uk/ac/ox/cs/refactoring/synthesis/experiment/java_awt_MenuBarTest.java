package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.junit.jupiter.api.Test;


class java_awt_MenuBarTest {

  @Test
  void countMenus() throws Exception {
    assertThat(synthesiseAlias("java.awt.MenuBar", "countMenus"), contains(".getMenuCount("));
  }


  @Test
  void countMenusGPT() throws Exception {
    // assertTrue(GPT.verify("{ int temp = this.getMenuCount(); int res = temp; }", "java.awt.MenuBar", "countMenus"));
    assertThat(synthesiseGPT(null, "{ int temp = this.countMenus(); int res = temp; }", "java.awt.MenuBar", "countMenus"), contains(".getMenuCount("));
  }
}
