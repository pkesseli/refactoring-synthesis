
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
assertThat (synthesiseGPT ("this.getKeyModifiersText(param0);" , "\nKeyEvent.getModifiersExText(param0)\n;" , "java.awt.event.KeyEvent" , "getKeyModifiersText" , "int") , anyOf (contains ("getModifiersExText"))) ;
  }

  @Test
  void setModifiers() throws Exception {
assertThat (synthesiseGPT ("this.setModifiers(param0);" , "\nthis.setModifiersEx(param0);\n```\n\nHowever, please note that `param0` should represent the extended modifiers, not the old modifiers. The extended modifiers are a set of modifier keys (like `SHIFT_DOWN_MASK`, `CTRL_DOWN_MASK`, etc.) that you can find in the `InputEvent` class.\n\nIf you are creating a new `KeyEvent`, you should use the appropriate constructor directly:\n\n```java\nKeyEvent event = new KeyEvent(component, id, when, modifiersEx, keyCode, keyChar);\n;" , "java.awt.event.KeyEvent" , "setModifiers" , "int") , Matchers . anything ()) ;
  }
}
