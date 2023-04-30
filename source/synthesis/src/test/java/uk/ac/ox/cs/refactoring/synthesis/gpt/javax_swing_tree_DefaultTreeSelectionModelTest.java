
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_tree_DefaultTreeSelectionModelTest {
  @Test
  void notifyPathChange() throws Exception {
    assertThat(synthesiseGPT("this.notifyPathChange(a, b);\n", "TreeSelectionEvent event = new TreeSelectionEvent(this, b, true, null, null);\nthis.fireValueChanged(event);\n", "javax.swing.tree.DefaultTreeSelectionModel", "notifyPathChange", "Vector&lt;?&gt;", "TreePath"), Matchers.anything());
  }
}