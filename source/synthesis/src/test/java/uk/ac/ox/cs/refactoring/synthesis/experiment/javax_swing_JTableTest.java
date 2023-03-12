
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_swing_JTableTest {
  // refactor to constructor, can this be handled?
  @Test
  void createScrollPaneForTable() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JTable", "createScrollPaneForTable", "javax.swing.JTable"), anyOf(contains("new JScrollPane")));
  }

  @Test
  void sizeColumnsToFit() throws Exception {
    assertThat(synthesiseAlias("javax.swing.JTable", "sizeColumnsToFit", "boolean"), anyOf(contains("doLayout")));
  }
}