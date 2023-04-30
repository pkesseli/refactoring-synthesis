
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_AWTKeyStrokeTest {
  @Test
  void registerSubclass() throws Exception {
    assertThat(synthesiseGPT("<code before refactoring here>\nthis.registerSubclass(a);\n<code after refactoring here>\nAWTKeyStroke awtKeyStroke = AWTKeyStroke.getAWTKeyStroke(a.getName());\nAWTKeyStroke.registerSubclass(MyAWTKeyStroke.class);\nNote: `MyAWTKeyStroke` is a custom subclass of `AWTKeyStroke` that you need to create.\n", "", "java.awt.AWTKeyStroke", "registerSubclass", "Class&lt;?&gt;"), Matchers.anything());
  }
}