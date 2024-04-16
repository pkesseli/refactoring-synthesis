
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_CheckboxGroupTest {
  @Test
  void getCurrent() throws Exception {
assertThat (synthesiseNeural ("getCurrent" , "this.getCurrent();" , "\nthis.getSelectedCheckbox();\n" , "java.awt.CheckboxGroup" , "getCurrent") , anyOf (contains ("getSelectedCheckbox"))) ;
  }

  @Test
  void setCurrent() throws Exception {
assertThat (synthesiseNeural ("setCurrent" , "this.setCurrent(param0);" , "\nthis.setSelectedCheckbox(param0);\n" , "java.awt.CheckboxGroup" , "setCurrent" , "java.awt.Checkbox") , anyOf (contains ("setSelectedCheckbox"))) ;
  }
}
