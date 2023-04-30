
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JTableTest {
  @Test
  void createScrollPaneForTable() throws Exception {
    assertThat(synthesiseGPT("JScrollPane scrollPane = this.createScrollPaneForTable(a);\n\n", "JScrollPane scrollPane = new JScrollPane(a);\n", "javax.swing.JTable", "createScrollPaneForTable", "javax.swing.JTable"), anyOf(contains("new JScrollPane")));
  }

  @Test
  void sizeColumnsToFit() throws Exception {
    assertThat(synthesiseGPT("this.sizeColumnsToFit(a);\n\n", "this.doLayout();\n", "javax.swing.JTable", "sizeColumnsToFit", "boolean"), anyOf(contains("doLayout")));
  }
}