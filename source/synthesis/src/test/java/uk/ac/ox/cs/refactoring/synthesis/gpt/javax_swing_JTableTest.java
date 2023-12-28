
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
assertThat (synthesiseGPT ("createScrollPaneForTable" , "this.createScrollPaneForTable(param0);" , "\nnew JScrollPane(param0);\n" , "javax.swing.JTable" , "createScrollPaneForTable" , "javax.swing.JTable") , anyOf (contains ("new JScrollPane"))) ;
  }

  @Test
  void sizeColumnsToFit() throws Exception {
assertThat (synthesiseGPT ("sizeColumnsToFit" , "this.sizeColumnsToFit(param0);" , "\nthis.doLayout();\n" , "javax.swing.JTable" , "sizeColumnsToFit" , "boolean") , anyOf (contains ("doLayout"))) ;
  }
}
