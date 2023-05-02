
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_AWTKeyStrokeTest {
  @Test
  void registerSubclass() throws Exception {
    assertThat(synthesiseGPT("this.registerSubclass(a);\n\n", "AWTKeyStroke awtKeyStroke = AWTKeyStroke.getAWTKeyStroke(a.getName());\nAWTKeyStroke.registerSubclass(awtKeyStroke.getClass());\n", "java.awt.AWTKeyStroke", "registerSubclass", "Class&lt;?&gt;"), Matchers.anything());
  }
}