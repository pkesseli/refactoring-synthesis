
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_event_KeyEventTest {
  @Test
  void getKeyModifiersText() throws Exception {
    assertThat(synthesiseGPT("String modifiers = this.getKeyModifiersText(a);\n\n", "String modifiers = InputEvent.getModifiersExText(a);\n", "java.awt.event.KeyEvent", "getKeyModifiersText", "int"), anyOf(contains("getModifiersExText")));
  }

  @Test
  void setModifiers() throws Exception {
    assertThat(synthesiseGPT("this.setModifiers(a);\n\n", "this.setModifiersEx(a);\n", "java.awt.event.KeyEvent", "setModifiers", "int"), Matchers.anything());
  }
}