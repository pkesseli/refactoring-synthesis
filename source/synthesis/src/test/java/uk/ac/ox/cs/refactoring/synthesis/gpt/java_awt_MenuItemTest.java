
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_MenuItemTest {
  @Test
  void disable() throws Exception {
assertThat (synthesiseNeural ("disable" , "this.disable();" , "\nthis.setEnabled(false);\n" , "java.awt.MenuItem" , "disable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable1() throws Exception {
assertThat (synthesiseNeural ("enable1" , "this.enable();" , "\nthis.setEnabled(true);\n" , "java.awt.MenuItem" , "enable") , anyOf (contains ("setEnabled"))) ;
  }

  @Test
  void enable2() throws Exception {
assertThat (synthesiseNeural ("enable2" , "this.enable(param0);" , "\nthis.setEnabled(param0);\n" , "java.awt.MenuItem" , "enable" , "boolean") , anyOf (contains ("setEnabled"))) ;
  }
}
