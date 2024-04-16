
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_MenuContainerTest {
  @Test
  void postEvent() throws Exception {
assertThat (synthesiseNeural ("postEvent" , "this.postEvent(param0);" , "\nthis.dispatchEvent(param0);\n" , "java.awt.MenuContainer" , "postEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }
}
