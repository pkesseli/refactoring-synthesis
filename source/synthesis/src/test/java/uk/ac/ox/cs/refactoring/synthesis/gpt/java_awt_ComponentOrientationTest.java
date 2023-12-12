
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ComponentOrientationTest {
  @Test
  void getOrientation() throws Exception {
assertThat (synthesiseGPT ("getOrientation" , "this.getOrientation(param0);" , "\n// Assuming 'this' is a Component and 'param0' is a ResourceBundle\nComponentOrientation orientation = ComponentOrientation.getOrientation(param0.getLocale());\nthis.setComponentOrientation(orientation);\n" , "java.awt.ComponentOrientation" , "getOrientation" , "java.util.ResourceBundle") , anyOf (contains ("getOrientation"))) ;
  }
}
