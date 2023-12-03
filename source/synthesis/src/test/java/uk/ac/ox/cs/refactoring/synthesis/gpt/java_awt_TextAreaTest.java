
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_TextAreaTest {
  @Test
  void appendText() throws Exception {
assertThat (synthesiseGPT ("this.appendText(param0);" , "\nthis.insert(param0, this.getText().length())\n;" , "java.awt.TextArea" , "appendText" , "java.lang.String") , anyOf (contains ("append"))) ;
  }

  @Test
  void insertText() throws Exception {
assertThat (synthesiseGPT ("this.insertText(param0, param1);" , "" , "java.awt.TextArea" , "insertText" , "java.lang.String" , "int") , anyOf (contains ("insert"))) ;
  }

  @Test
  void minimumSize1() throws Exception {
assertThat (synthesiseGPT ("this.minimumSize();" , "\nthis.getMinimumSize(0,0)\n;" , "java.awt.TextArea" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void minimumSize2() throws Exception {
assertThat (synthesiseGPT ("this.minimumSize(param0, param1);" , "this.getPreferredSize();" , "java.awt.TextArea" , "minimumSize" , "int" , "int") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void preferredSize1() throws Exception {
assertThat (synthesiseGPT ("this.preferredSize();" , "\nthis.getPreferredSize(0,0)\n;" , "java.awt.TextArea" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void preferredSize2() throws Exception {
assertThat (synthesiseGPT ("this.preferredSize(param0, param1);" , "" , "java.awt.TextArea" , "preferredSize" , "int" , "int") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void replaceText() throws Exception {
assertThat (synthesiseGPT ("this.replaceText(param0, param1, param2);" , "\nthis.replaceRange(param0, param1, param2);\n;" , "java.awt.TextArea" , "replaceText" , "java.lang.String" , "int" , "int") , anyOf (contains ("replaceRange"))) ;
  }
}
