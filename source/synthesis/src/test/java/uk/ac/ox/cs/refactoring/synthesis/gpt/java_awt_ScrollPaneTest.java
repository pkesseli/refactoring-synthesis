
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ScrollPaneTest {
  @Test
  void layout() throws Exception {
assertThat (synthesiseNeural ("layout" , "this.layout();" , "\nthis.doLayout();\n" , "java.awt.ScrollPane" , "layout") , anyOf (contains ("doLayout"))) ;
  }
}
