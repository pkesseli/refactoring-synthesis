
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.junit.jupiter.api.Test;

class java_awt_RectangleTest {
  @Test
  void inside() throws Exception {
    assertThat(synthesiseGPT("if (this.inside(a, b)) {\n    // do something\n}\n\n", "if (this.contains(a, b)) {\n    // do something\n}\n", "java.awt.Rectangle", "inside", "int", "int"), anyOf(contains("contains")));
  }

  @Test
  void move() throws Exception {
    assertThat(synthesiseGPT("this.move(a, b);\n\n", "this.setLocation(a, b);\n", "java.awt.Rectangle", "move", "int", "int"), anyOf(contains("setLocation")));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseGPT("this.reshape(a, b, c, d);\n\n", "this.setBounds(a, b, c, d);\n", "java.awt.Rectangle", "reshape", "int", "int", "int", "int"), anyOf(contains("setBounds")));
  }

  @Test
  void resize() throws Exception {
    assertThat(synthesiseGPT("this.resize(a, b);\n", "this.setSize(a, b);\n", "java.awt.Rectangle", "resize", "int", "int"), anyOf(contains("setSize")));
  }
}