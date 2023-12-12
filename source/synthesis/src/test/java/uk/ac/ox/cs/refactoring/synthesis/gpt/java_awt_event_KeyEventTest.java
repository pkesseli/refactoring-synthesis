
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
assertThat (synthesiseGPT ("getKeyModifiersText" , "this.getKeyModifiersText(param0);" , "\nKeyEvent.getModifiersExText(param0);\n" , "java.awt.event.KeyEvent" , "getKeyModifiersText" , "int") , anyOf (contains ("getModifiersExText"))) ;
  }

  @Test
  void setModifiers() throws Exception {
assertThat (synthesiseGPT ("setModifiers" , "this.setModifiers(param0);" , "\nKeyEvent newEvent = new KeyEvent(\n    this, // the source component\n    KeyEvent.KEY_PRESSED, // the type of key event\n    System.currentTimeMillis(), // the time the event occurred\n    param0, // the modifiers\n    KeyEvent.VK_UNDEFINED, // the key code (undefined if not known)\n    KeyEvent.CHAR_UNDEFINED // the key char (undefined if not known)\n);\n" , "java.awt.event.KeyEvent" , "setModifiers" , "int") , Matchers . anything ()) ;
  }
}
