
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_TableViewTest {
  @Test
  void createTableCell() throws Exception {
assertThat (synthesiseGPT ("createTableCell" , "this.createTableCell(param0);" , "" , "javax.swing.text.TableView" , "createTableCell" , "javax.swing.text.Element") , Matchers . anything ()) ;
  }
}
