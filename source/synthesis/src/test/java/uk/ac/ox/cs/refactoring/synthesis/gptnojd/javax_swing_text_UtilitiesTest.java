
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_UtilitiesTest {
  @Test
  void drawTabbedText() throws Exception {
    assertThat(synthesiseGPT("this.drawTabbedText(a, b, c, d, e, f);\n\n", "TabRenderer renderer = new TabRenderer();\nrenderer.drawTabbedText(a, b, c, d, e, f);\nNote: `TabRenderer` is a custom class that implements the `TabExpander` interface and provides the implementation for the `nextTabStop` method.\n", "javax.swing.text.Utilities", "drawTabbedText", "javax.swing.text.Segment", "int", "int", "java.awt.Graphics", "javax.swing.text.TabExpander", "int"), anyOf(contains("drawTabbedText")));
  }

  @Test
  void getBreakLocation() throws Exception {
    assertThat(synthesiseGPT("int breakLocation = javax.swing.text.Utilities.getBreakLocation(a, b, c, d, e, f);\n\n", "int breakLocation = javax.swing.text.Utilities.getBreakLocation(a, b.getFontRenderContext(), c, d, e, f);\n", "javax.swing.text.Utilities", "getBreakLocation", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getBreakLocation")));
  }

  @Test
  void getPositionAbove() throws Exception {
    assertThat(synthesiseGPT("int positionAbove = this.getPositionAbove(a, b, c);\n\n", "Rectangle rectangle = a.getUI().modelToView(a, b);\nint positionAbove = a.getUI().viewToModel(a, new Point(rectangle.x, rectangle.y - 1));\n", "javax.swing.text.Utilities", "getPositionAbove", "javax.swing.text.JTextComponent", "int", "int"), anyOf(contains("getPositionAbove")));
  }

  @Test
  void getPositionBelow() throws Exception {
    assertThat(synthesiseGPT("int pos = this.getPositionBelow(a, b, c);\n\n", "int pos = javax.swing.text.Utilities.getPositionBelow(a, b, c, javax.swing.text.DefaultCaret.NEXT);\n", "javax.swing.text.Utilities", "getPositionBelow", "javax.swing.text.JTextComponent", "int", "int"), anyOf(contains("getPositionBelow")));
  }

  @Test
  void getTabbedTextOffset1() throws Exception {
    assertThat(synthesiseGPT("int offset = javax.swing.text.Utilities.getTabbedTextOffset(a, b, c, d, e, f);\n\n", "int offset = javax.swing.text.Utilities.getTabbedTextOffset(null, a, b, c, d, e, f);\n", "javax.swing.text.Utilities", "getTabbedTextOffset", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getTabbedTextOffset")));
  }

  @Test
  void getTabbedTextOffset2() throws Exception {
    assertThat(synthesiseGPT("int offset = javax.swing.text.Utilities.getTabbedTextOffset(a, b, c, d, e, f, g);\n\n", "int offset = javax.swing.text.Utilities.getTabbedTextOffset(a, b, c, d, e, f, g, true, true);\n", "javax.swing.text.Utilities", "getTabbedTextOffset", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int", "boolean"), anyOf(contains("getTabbedTextOffset")));
  }

  @Test
  void getTabbedTextWidth() throws Exception {
    assertThat(synthesiseGPT("int width = javax.swing.text.Utilities.getTabbedTextWidth(a, b, c, d, e);\n\n", "TabExpander expander = d;\nint width = (int) expander.nextTabStop(c, e) - c;\n", "javax.swing.text.Utilities", "getTabbedTextWidth", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getTabbedTextWidth")));
  }
}