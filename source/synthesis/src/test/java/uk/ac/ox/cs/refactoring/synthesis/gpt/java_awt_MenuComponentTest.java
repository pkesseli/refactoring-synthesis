
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_MenuComponentTest {
  @Test
  void postEvent() throws Exception {
assertThat (synthesiseGPT ("this.postEvent(param0);" , "\nthis.dispatchEvent(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, null));\n;" , "java.awt.MenuComponent" , "postEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }
}
