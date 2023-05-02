
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_FrameTest {
  @Test
  void getCursorType() throws Exception {
    assertThat(synthesiseGPT("int cursorType = this.getCursorType();\n\n", "Cursor cursor = this.getCursor();\nint cursorType = cursor.getType();\n", "java.awt.Frame", "getCursorType"), anyOf(contains("getCursor")));
  }

  @Test
  void setCursor() throws Exception {
    assertThat(synthesiseGPT("this.setCursor(a);\n\n", "Cursor cursor = Cursor.getPredefinedCursor(a);\nthis.setCursor(cursor);\n", "java.awt.Frame", "setCursor", "int"), anyOf(contains("setCursor")));
  }
}