
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_FrameTest {
  @Test
  void getCursorType() throws Exception {
assertThat (synthesiseGPT ("getCursorType" , "this.getCursorType();" , "\nthis.getCursor().getType()\n;" , "java.awt.Frame" , "getCursorType") , anyOf (contains ("getCursor"))) ;
  }

  @Test
  void setCursor() throws Exception {
assertThat (synthesiseGPT ("setCursor" , "this.setCursor(param0);" , "\nthis.setCursor(Cursor.getPredefinedCursor(param0));\n" , "java.awt.Frame" , "setCursor" , "int") , anyOf (contains ("setCursor"))) ;
  }
}
