
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_table_TableColumnTest {
  @Test
  void disableResizedPosting() throws Exception {
    assertThat(synthesiseGPT("this.disableResizedPosting();\n\n", "this.getResizingColumn().setResizable(false);\n", "javax.swing.table.TableColumn", "disableResizedPosting"), Matchers.anything());
  }

  @Test
  void enableResizedPosting() throws Exception {
    assertThat(synthesiseGPT("this.enableResizedPosting();\n\n", "this.setResizingAllowed(true);\n", "javax.swing.table.TableColumn", "enableResizedPosting"), Matchers.anything());
  }
}