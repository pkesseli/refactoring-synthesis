
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_WindowTest {
  @Test
  void applyResourceBundle1() throws Exception {
    assertThat(synthesiseGPT("this.applyResourceBundle(a);\n\n", "ResourceBundle bundle = ResourceBundle.getBundle(a);\napplyComponentOrientation(ComponentOrientation.getOrientation(bundle.getLocale()));\n", "java.awt.Window", "applyResourceBundle", "java.lang.String"), anyOf(contains("applyComponentOrientation")));
  }

  @Test
  void applyResourceBundle2() throws Exception {
    assertThat(synthesiseGPT("this.applyResourceBundle(a);\n\n", "this.applyComponentOrientation(ComponentOrientation.getOrientation(a.getLocale()));\n", "java.awt.Window", "applyResourceBundle", "java.util.ResourceBundle"), anyOf(contains("applyComponentOrientation")));
  }

  @Test
  void hide() throws Exception {
    assertThat(synthesiseGPT("this.hide();\n\n", "this.setVisible(false);\n", "java.awt.Window", "hide"), anyOf(contains("setVisible")));
  }

  @Test
  void postEvent() throws Exception {
    assertThat(synthesiseGPT("this.postEvent(a);\n", "Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(a);\n", "java.awt.Window", "postEvent", "java.awt.Event"), anyOf(contains("dispatchEvent")));
  }

  @Test
  void reshape() throws Exception {
    assertThat(synthesiseGPT("this.reshape(a, b, c, d);\n\n", "this.setBounds(a, b, c, d);\n", "java.awt.Window", "reshape", "int", "int", "int", "int"), anyOf(contains("setBounds")));
  }

  @Test
  void show() throws Exception {
    assertThat(synthesiseGPT("this.show();\n\n", "this.setVisible(true);\n", "java.awt.Window", "show"), anyOf(contains("setVisible")));
  }
}