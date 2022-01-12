
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.mapsTo;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_ComponentTest {

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void action() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "action", "java.awt.Event", "java.lang.Object"), contains("???"));
  }

  @Test
  void bounds() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "bounds"), contains(".getBounds("));
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void deliverEvent() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "deliverEvent", "java.awt.Event"), contains(".dispatchEvent("));
  }

  @Test
  void disable() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "disable"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_ComponentTest::isDisabled, fromEnabled(true)),
            mapsTo(java_awt_ComponentTest::isDisabled, fromEnabled(false))));
  }

  @Test
  void enable() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "enable"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_ComponentTest::isEnabled, fromEnabled(true)),
            mapsTo(java_awt_ComponentTest::isEnabled, fromEnabled(false))));
  }

  @Test
  void enableBoolean() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "enable", "boolean"),
        allOf(
            contains(".setEnabled("),
            mapsTo(java_awt_ComponentTest::isEnabled, fromEnabled(true), true),
            mapsTo(java_awt_ComponentTest::isEnabled, fromEnabled(false), true),
            mapsTo(java_awt_ComponentTest::isDisabled, fromEnabled(true), false),
            mapsTo(java_awt_ComponentTest::isDisabled, fromEnabled(false), false)));
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void gotFocus() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "gotFocus", "java.awt.Event"), contains(".processFocusEvent("));
  }

  @Test
  void hide() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "hide"),
        allOf(
            contains(".setVisible("),
            mapsTo(java_awt_ComponentTest::isHidden, fromVisible(true)),
            mapsTo(java_awt_ComponentTest::isHidden, fromVisible(false))));
  }

  @Test
  void inside() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "inside", "int", "int"),
        allOf(
            contains(".contains("),
            mapsTo(true, fromDimension(10, 10), 5, 5),
            mapsTo(false, fromDimension(10, 10), 20, 20)));
  }

  @Test
  void isFocusTraversable() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "isFocusTraversable"),
        allOf(
            contains(".isFocusable("),
            mapsTo(true, fromFocusTraversable(true)),
            mapsTo(false, fromFocusTraversable(false))));
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void keyDown() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "keyDown", "java.awt.Event", "int"),
        contains(".processKeyEvent("));
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void keyUp() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "keyUp", "java.awt.Event", "int"),
        contains(".processKeyEvent("));
  }

  @Test
  void layout() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "layout"), contains(".doLayout()"));
  }

  @Test
  void locate() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "locate", "int", "int"), contains(".getComponentAt("));
  }

  @Test
  void location() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "location"), contains(".getLocation()"));
  }

  @Test
  void show() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "show"),
        allOf(
            contains(".setVisible("),
            mapsTo(java_awt_ComponentTest::isVisible, fromVisible(true)),
            mapsTo(java_awt_ComponentTest::isVisible, fromVisible(false))));
  }

  @Test
  void showBoolean() throws Exception {
    assertThat(synthesiseAlias("java.awt.Component", "show", "boolean"),
        allOf(
            contains(".setVisible("),
            mapsTo(java_awt_ComponentTest::isVisible, fromVisible(true), true),
            mapsTo(java_awt_ComponentTest::isVisible, fromVisible(false), true),
            mapsTo(java_awt_ComponentTest::isHidden, fromVisible(true), false),
            mapsTo(java_awt_ComponentTest::isHidden, fromVisible(false), false)));
  }

  private static Component fromFocusTraversable(final boolean isFocusTraversable) {
    final Component component = new Panel();
    component.setFocusable(isFocusTraversable);
    return component;
  }

  private static Component fromEnabled(final boolean enabled) {
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

  private static Component fromVisible(final boolean isVisible) {
    final Component component = new Panel();
    component.setVisible(isVisible);
    return component;
  }

  private static boolean isVisible(final Component component, final Void result) {
    return component.isVisible();
  }

  private static boolean isHidden(final Component component, final Void result) {
    return !component.isVisible();
  }

  private static Component fromDimension(final int width, final int height) {
    final Component component = new Panel();
    component.setSize(new Dimension(width, height));
    return component;
  }
}
