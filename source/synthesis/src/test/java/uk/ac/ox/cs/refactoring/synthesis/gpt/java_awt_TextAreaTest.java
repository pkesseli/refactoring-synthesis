
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
assertThat (synthesiseGPT ("appendText" , "this.appendText(param0);" , "\nthis.insert(param0, this.getText().length());\n" , "java.awt.TextArea" , "appendText" , "java.lang.String") , anyOf (contains ("append"))) ;
  }

  @Test
  void insertText() throws Exception {
assertThat (synthesiseGPT ("insertText" , "this.insertText(param0, param1);" , "\nthis.insert(param0, param1);\n" , "java.awt.TextArea" , "insertText" , "java.lang.String" , "int") , anyOf (contains ("insert"))) ;
  }

  @Test
  void minimumSize1() throws Exception {
assertThat (synthesiseGPT ("minimumSize1" , "this.minimumSize();" , "\nthis.getMinimumSize(this.getColumns(), this.getRows())\n;" , "java.awt.TextArea" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void minimumSize2() throws Exception {
assertThat (synthesiseGPT ("minimumSize2" , "this.minimumSize(param0, param1);" , "\nthis.getPreferredSize()\n;" , "java.awt.TextArea" , "minimumSize" , "int" , "int") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void preferredSize1() throws Exception {
assertThat (synthesiseGPT ("preferredSize1" , "this.preferredSize();" , "\nthis.getPreferredSize()\n;" , "java.awt.TextArea" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void preferredSize2() throws Exception {
assertThat (synthesiseGPT ("preferredSize2" , "this.preferredSize(param0, param1);" , "\nthis.setPreferredSize(new Dimension(param0, param1));\nDimension preferredSize = this.getPreferredSize();\n" , "java.awt.TextArea" , "preferredSize" , "int" , "int") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void replaceText() throws Exception {
assertThat (synthesiseGPT ("replaceText" , "this.replaceText(param0, param1, param2);" , "\nthis.replaceRange(param0, param1, param1 + param2);\n" , "java.awt.TextArea" , "replaceText" , "java.lang.String" , "int" , "int") , anyOf (contains ("replaceRange"))) ;
  }
}
