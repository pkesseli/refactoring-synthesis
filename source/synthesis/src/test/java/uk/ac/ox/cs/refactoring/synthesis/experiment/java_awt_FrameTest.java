package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_FrameTest {

  @Test
  @Disabled("Signatures do not match. Minimal grammar does not try to explore methods of return type at that point, though could probably be extended to do so.")
  void getCursorType() throws Exception {
    assertThat(synthesiseAlias("java.awt.Frame", "getCursorType"),
        allOf(contains(".getCursor("), contains(".getType(")));
  }

  @Test
  @Disabled("Fails because our type-dependent instruction set generation would need to recognise that Frame instanceof Component. Future work.")
  void setCursorType() throws Exception {
    assertThat(synthesiseAlias("java.awt.Frame", "setCursor", "int"),
        allOf(contains(".setCursor("), contains("Cursor.getPredefinedCursor(")));
  }
}
