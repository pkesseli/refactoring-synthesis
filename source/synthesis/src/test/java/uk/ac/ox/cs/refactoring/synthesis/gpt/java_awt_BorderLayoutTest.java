
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_BorderLayoutTest {
  @Test
  void addLayoutComponent() throws Exception {
assertThat (synthesiseNeural ("addLayoutComponent" , "this.addLayoutComponent(param0, param1);" , "\nthis.addLayoutComponent(param1, param0);\n" , "java.awt.BorderLayout" , "addLayoutComponent" , "java.lang.String" , "java.awt.Component") , anyOf (contains ("addLayoutComponent"))) ;
  }
}
