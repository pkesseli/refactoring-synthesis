
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JPasswordFieldTest {
  @Test
  void getText1() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JPasswordField", "getText"), anyOf(contains("getPassword")));
  }

  @Test
  void getText2() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JPasswordField", "getText", "int", "int"), anyOf(contains("getPassword")));
  }
}