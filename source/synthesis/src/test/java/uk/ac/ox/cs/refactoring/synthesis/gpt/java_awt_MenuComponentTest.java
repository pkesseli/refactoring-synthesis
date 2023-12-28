
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
assertThat (synthesiseGPT ("postEvent" , "this.postEvent(param0);" , "\nfinal Event event = param0;\nfinal Component component = this;\nEventQueue.invokeLater(() -> component.dispatchEvent(event));\n" , "java.awt.MenuComponent" , "postEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }
}
