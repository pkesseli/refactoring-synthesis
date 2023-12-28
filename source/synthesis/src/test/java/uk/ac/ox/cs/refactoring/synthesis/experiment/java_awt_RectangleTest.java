package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.awt.Rectangle;

import org.junit.jupiter.api.Test;


class java_awt_RectangleTest {

  @Test
  void inside() throws Exception {
    assertThat(synthesiseAlias("java.awt.Rectangle", "inside", "int", "int"),
        allOf(
            contains(".contains("),
            mapsTo(true, new Rectangle(0, 0, 10, 20), 5, 10),
            mapsTo(false, new Rectangle(0, 0, 9, 9), 5, 10),
            mapsTo(false, new Rectangle(0, 0, 9, 9), 10, 5)));
  }

  @Test
  void move() throws Exception {
    assertThat(synthesiseAlias("java.awt.Rectangle", "move", "int", "int"), contains(".setLocation("));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseAlias("java.awt.Rectangle", "reshape", "int", "int", "int", "int"), contains(".setBounds("));
  }

  @Test
  void resize() throws Exception {
    assertThat(synthesiseAlias("java.awt.Rectangle", "resize", "int", "int"), contains(".setSize("));
  }
}
