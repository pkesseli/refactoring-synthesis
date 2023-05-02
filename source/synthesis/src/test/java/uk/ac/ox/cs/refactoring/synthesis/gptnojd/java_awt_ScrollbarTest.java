
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ScrollbarTest {
  @Test
  void getLineIncrement() throws Exception {
    assertThat(synthesiseGPT("int lineIncrement = this.getLineIncrement();\n\n", "int lineIncrement = this.getUnitIncrement();\n", "java.awt.Scrollbar", "getLineIncrement"), anyOf(contains("getUnitIncrement")));
  }

  @Test
  void getPageIncrement() throws Exception {
    assertThat(synthesiseGPT("int pageIncrement = this.getPageIncrement();\n\n", "int pageIncrement = this.getUnitIncrement() * 10;\n", "java.awt.Scrollbar", "getPageIncrement"), anyOf(contains("getBlockIncrement")));
  }

  @Test
  void getVisible() throws Exception {
    assertThat(synthesiseGPT("boolean visible = this.getVisible();\n\n", "boolean visible = this.isVisible();\n", "java.awt.Scrollbar", "getVisible"), anyOf(contains("getVisibleAmount")));
  }

  @Test
  void setLineIncrement() throws Exception {
    assertThat(synthesiseGPT("this.setLineIncrement(a);\n", "this.setUnitIncrement(a);\n", "java.awt.Scrollbar", "setLineIncrement", "int"), anyOf(contains("setUnitIncrement")));
  }

  @Test
  void setPageIncrement() throws Exception {
    assertThat(synthesiseGPT("this.setPageIncrement(a);\n", "this.setUnitIncrement(a);\n", "java.awt.Scrollbar", "setPageIncrement", "int"), anyOf(contains("setBlockIncrement")));
  }
}