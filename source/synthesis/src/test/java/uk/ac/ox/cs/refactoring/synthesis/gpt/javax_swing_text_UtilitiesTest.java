
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_UtilitiesTest {
  @Test
  void drawTabbedText() throws Exception {
    assertThat(synthesiseGPT("this.drawTabbedText(a, b, c, d, e, f);\n", "Utilities.drawTabbedText(a, (float)b, (float)c, (Graphics2D)d, e, f);\n", "javax.swing.text.Utilities", "drawTabbedText", "javax.swing.text.Segment", "int", "int", "java.awt.Graphics", "javax.swing.text.TabExpander", "int"), anyOf(contains("drawTabbedText")));
  }

  @Test
  void getBreakLocation() throws Exception {
    assertThat(synthesiseGPT("int breakLocation = Utilities.getBreakLocation(a, b, c, d, e, f);\n\n", "float breakLocation = Utilities.getBreakLocation(a, b, (float) c, (float) d, e, f);\n", "javax.swing.text.Utilities", "getBreakLocation", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getBreakLocation")));
  }

  @Test
  void getPositionAbove() throws Exception {
    assertThat(synthesiseGPT("int position = Utilities.getPositionAbove(a, b, c);\n\n", "int position = Utilities.getPositionAbove(a, b, (float) c);\n", "javax.swing.text.Utilities", "getPositionAbove", "javax.swing.text.JTextComponent", "int", "int"), anyOf(contains("getPositionAbove")));
  }

  @Test
  void getPositionBelow() throws Exception {
    assertThat(synthesiseGPT("int pos = Utilities.getPositionBelow(a, b, c);\n\n", "int pos = Utilities.getPositionBelow(a, b, (float) c);\n", "javax.swing.text.Utilities", "getPositionBelow", "javax.swing.text.JTextComponent", "int", "int"), anyOf(contains("getPositionBelow")));
  }

  @Test
  void getTabbedTextOffset1() throws Exception {
    assertThat(synthesiseGPT("int offset = javax.swing.text.Utilities.getTabbedTextOffset(a, b, c, d, e, f);\n\n", "int offset = (int) javax.swing.text.Utilities.getTabbedTextOffset(a, b, (float) c, (float) d, e, f, false);\n", "javax.swing.text.Utilities", "getTabbedTextOffset", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getTabbedTextOffset")));
  }

  @Test
  void getTabbedTextOffset2() throws Exception {
    assertThat(synthesiseGPT("int offset = Utilities.getTabbedTextOffset(a, b, c, d, e, f, g);\n\n", "float offset = Utilities.getTabbedTextOffset(a, b, (float)c, (float)d, e, f, g);\n", "javax.swing.text.Utilities", "getTabbedTextOffset", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int", "boolean"), anyOf(contains("getTabbedTextOffset")));
  }

  @Test
  void getTabbedTextWidth() throws Exception {
    assertThat(synthesiseGPT("this.getTabbedTextWidth(a, b, c, d, e);\n", "Utilities.getTabbedTextWidth(a, b, (float) c, d, e);\n", "javax.swing.text.Utilities", "getTabbedTextWidth", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getTabbedTextWidth")));
  }
}