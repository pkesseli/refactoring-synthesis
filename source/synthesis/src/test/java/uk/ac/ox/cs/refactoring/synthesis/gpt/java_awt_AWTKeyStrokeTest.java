
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_awt_AWTKeyStrokeTest {
  @Disabled("No replacement")
  @Test
  void registerSubclass() throws Exception {
assertThat (synthesiseGPT ("registerSubclass" , "this.registerSubclass(param0);" , "\nAWTKeyStroke awtKeyStroke = (AWTKeyStroke) param0.newInstance();\nAWTKeyStroke.registerSubclass(awtKeyStroke.getClass());\n" , "java.awt.AWTKeyStroke" , "registerSubclass" , "java.lang.Class") , Matchers . anything ()) ;
  }
}
