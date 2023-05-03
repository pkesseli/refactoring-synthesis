
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_text_html_HTMLEditorKit_InsertHTMLTextActionTest {
  @Test
  void insertAtBoundry() throws Exception {
    assertThat(synthesiseAlias("javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction", "insertAtBoundry", "javax.swing.JEditorPane", "javax.swing.text.html.HTMLDocument", "int", "javax.swing.text.Element", "java.lang.String", "javax.swing.text.html.HTML$Tag", "javax.swing.text.html.HTML$Tag"), contains("insertAtBoundary"));
  }
}