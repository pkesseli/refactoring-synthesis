
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_event_InputEventTest {
  @Test
  void getModifiers() throws Exception {
    assertThat(synthesiseGPT("int modifiers = this.getModifiers();\n\n", "int modifiers = this.getModifiersEx();\n", "java.awt.event.InputEvent", "getModifiers"), anyOf(contains("getModifiersEx")));
  }
}