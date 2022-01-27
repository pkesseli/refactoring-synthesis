
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.awt.Window;

import org.junit.jupiter.api.Test;

class java_awt_WindowTest {

  @Test
  void hide() throws Exception {
    assertThat(synthesiseAlias("java.awt.Window", "hide"),
        allOf(
            contains(".setVisible("),
            mapsTo(Components::isHidden, fromVisible(true)),
            mapsTo(Components::isHidden, fromVisible(false))));
  }

  @Test
  void postEvent() throws Exception {
    assertThat(synthesiseAlias("java.awt.Window", "postEvent", "java.awt.Event"), contains(".dispatchEvent("));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseAlias("java.awt.Window", "reshape", "int", "int", "int", "int"), contains(".setBounds("));
  }

  @Test
  void show() throws Exception {
    assertThat(synthesiseAlias("java.awt.Window", "show"),
        allOf(
            contains(".setVisible("),
            mapsTo(Components::isVisible, fromVisible(true)),
            mapsTo(Components::isVisible, fromVisible(false))));
  }

  private static Window fromVisible(final boolean isVisible) {
    final Window component = new Window(null);
    component.setVisible(isVisible);
    return component;
  }
}
