
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_table_TableColumnTest {
  @Disabled("No replacement")
  @Test
  void disableResizedPosting() throws Exception {
assertThat (synthesiseNeural ("disableResizedPosting" , "this.disableResizedPosting();" , "\n// There is no direct replacement for this method. \n// However, you can use the following code to achieve the same effect:\nthis.setResizable(false);\n" , "javax.swing.table.TableColumn" , "disableResizedPosting") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void enableResizedPosting() throws Exception {
assertThat (synthesiseNeural ("enableResizedPosting" , "this.enableResizedPosting();" , "\n// Deprecated method call\nthis.enableResizedPosting();\n\n// Alternative solution\nthis.setResizingAllowed(true);\n" , "javax.swing.table.TableColumn" , "enableResizedPosting") , Matchers . anything ()) ;
  }
}
