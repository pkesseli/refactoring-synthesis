
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_text_UtilitiesTest {
  @Test
  void drawTabbedText() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "drawTabbedText", "javax.swing.text.Segment", "int", "int", "java.awt.Graphics", "javax.swing.text.TabExpander", "int"), anyOf(contains("drawTabbedText")));
  }

  @Test
  void getBreakLocation() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "getBreakLocation", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getBreakLocation")));
  }

  @Test
  void getPositionAbove() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "getPositionAbove", "javax.swing.text.JTextComponent", "int", "int"), anyOf(contains("getPositionAbove")));
  }

  @Test
  void getPositionBelow() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "getPositionBelow", "javax.swing.text.JTextComponent", "int", "int"), anyOf(contains("getPositionBelow")));
  }

  @Test
  void getTabbedTextOffset1() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "getTabbedTextOffset", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getTabbedTextOffset")));
  }

  @Test
  void getTabbedTextOffset2() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "getTabbedTextOffset", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "int", "javax.swing.text.TabExpander", "int", "boolean"), anyOf(contains("getTabbedTextOffset")));
  }

  @Test
  void getTabbedTextWidth3() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.Utilities", "getTabbedTextWidth", "javax.swing.text.Segment", "java.awt.FontMetrics", "int", "javax.swing.text.TabExpander", "int"), anyOf(contains("getTabbedTextWidth")));
  }
}