
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_text_html_HTMLEditorKit_InsertHTMLTextActionTest {
  @Test
  void insertAtBoundry() throws Exception {
    assertThat(synthesiseGPT("this.insertAtBoundry(a, b, c, d, e, f, g);\n\n", "HTMLDocument doc = (HTMLDocument) a.getDocument();\ntry {\n    doc.insertAfterStart(d, e);\n} catch (BadLocationException ex) {\n    ex.printStackTrace();\n}\n", "javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction", "insertAtBoundry", "javax.swing.JEditorPane", "javax.swing.text.html.HTMLDocument", "int", "javax.swing.text.Element", "java.lang.String", "javax.swing.text.html.HTML$Tag", "javax.swing.text.html.HTML$Tag"), Matchers.anything());
  }
}