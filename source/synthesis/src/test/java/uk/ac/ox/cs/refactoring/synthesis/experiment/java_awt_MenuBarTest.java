package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.synthesis.presets.GPT;

class java_awt_MenuBarTest {

  @Test
  void countMenus() throws Exception {
    assertThat(synthesiseAlias("java.awt.MenuBar", "countMenus"), contains(".getMenuCount("));
  }


  @Test
  void countMenusGPT() throws Exception {
    assertTrue(GPT.verify("{ int tmp = this.getMenuCount(); }", "java.awt.MenuBar", "countMenus"));
  }
}
