
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.awt.Component;
import java.awt.Panel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_ComponentTest {

  @Test
  void bounds() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "bounds"), contains(".getBounds("));
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void deliverEvents() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "deliverEvent", "java.awt.Event"), contains(".dispatchEvent("));
  }

  @Test
  void disable() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "disable"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_ComponentTest::isDisabled, component(true)),
            mapsTo(java_awt_ComponentTest::isDisabled, component(false))));
  }

  @Test
  void enable() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "enable"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_ComponentTest::isEnabled, component(true)),
            mapsTo(java_awt_ComponentTest::isEnabled, component(false))));
  }

  private static Component component(final boolean enabled) {
    final Component component = new Panel();
    component.setEnabled(enabled);
    return component;
  }

  private static boolean isEnabled(final Component component, final Void result) {
    return component.isEnabled();
  }

  private static boolean isDisabled(final Component component, final Void result) {
    return !component.isEnabled();
  }
}
