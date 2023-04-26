
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_text_PasswordViewTest {
  @Test
  void drawEchoCharacter() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.PasswordView", "drawEchoCharacter", "java.awt.Graphics", "int", "int", "char"), contains("drawEchoCharacter"));
  }

  @Test
  void drawSelectedText() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.PasswordView", "drawSelectedText", "java.awt.Graphics", "int", "int", "int", "int"), contains("drawSelectedText"));
  }

  @Test
  void drawUnselectedText() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.PasswordView", "drawUnselectedText", "java.awt.Graphics", "int", "int", "int", "int"), contains("drawUnselectedText"));
  }
}
