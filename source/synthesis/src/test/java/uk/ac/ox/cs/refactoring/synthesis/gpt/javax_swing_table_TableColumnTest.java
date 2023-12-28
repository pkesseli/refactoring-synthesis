
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_table_TableColumnTest {
  @Disabled("No replacement")
  @Test
  void disableResizedPosting() throws Exception {
assertThat (synthesiseGPT ("disableResizedPosting" , "this.disableResizedPosting();" , "\nfinal TableColumn column = this;\ncolumn.setResizable(false);\n" , "javax.swing.table.TableColumn" , "disableResizedPosting") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void enableResizedPosting() throws Exception {
assertThat (synthesiseGPT ("enableResizedPosting" , "this.enableResizedPosting();" , "\n// Deprecated method call\nthis.enableResizedPosting();\n\n// Refactored code\nfinal TableColumn column = this;\nfinal boolean oldAuto = column.getAutoResizeMode() == TableColumn.AUTO_RESIZE_OFF;\ncolumn.setResizable(true);\ncolumn.setPreferredWidth(column.getWidth());\ncolumn.setResizable(false);\ncolumn.setResizable(oldAuto);\n" , "javax.swing.table.TableColumn" , "enableResizedPosting") , Matchers . anything ()) ;
  }
}
