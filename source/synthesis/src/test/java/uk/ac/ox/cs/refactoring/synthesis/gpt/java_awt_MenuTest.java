
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_MenuTest {
  @Test
  void countItems() throws Exception {
assertThat (synthesiseGPT ("this.countItems();" , "this.getMenuCount();" , "java.awt.Menu" , "countItems") , anyOf (contains ("getItemCount"))) ;
  }
}
