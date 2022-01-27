
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.awt.MenuItem;

import org.junit.jupiter.api.Test;

class java_awt_MenuItemTest {

  @Test
  void disable() throws Exception {
    assertThat(synthesiseAlias("java.awt.MenuItem", "disable"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_MenuItemTest::isDisabled, fromEnabled(true)),
            mapsTo(java_awt_MenuItemTest::isDisabled, fromEnabled(false))));
  }

  @Test
  void enable() throws Exception {
    assertThat(synthesiseAlias("java.awt.MenuItem", "enable"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_MenuItemTest::isEnabled, fromEnabled(true)),
            mapsTo(java_awt_MenuItemTest::isEnabled, fromEnabled(false))));
  }

  @Test
  void enableBoolean() throws Exception {
    assertThat(synthesiseAlias("java.awt.MenuItem", "enable", "boolean"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_MenuItemTest::isEnabled, fromEnabled(true), true),
            mapsTo(java_awt_MenuItemTest::isEnabled, fromEnabled(false), true),
            mapsTo(java_awt_MenuItemTest::isDisabled, fromEnabled(true), false),
            mapsTo(java_awt_MenuItemTest::isDisabled, fromEnabled(false), false)));
  }

  private static MenuItem fromEnabled(final boolean enabled) {
    final MenuItem component = new MenuItem();
    component.setEnabled(enabled);
    return component;
  }

  private static boolean isEnabled(final MenuItem component, final Void result) {
    return component.isEnabled();
  }

  private static boolean isDisabled(final MenuItem component, final Void result) {
    return !component.isEnabled();
  }
}
