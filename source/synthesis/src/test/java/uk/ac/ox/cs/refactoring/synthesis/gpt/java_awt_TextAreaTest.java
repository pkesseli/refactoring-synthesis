
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_TextAreaTest {
  @Test
  void appendText() throws Exception {
assertThat (synthesiseNeural ("appendText" , "this.appendText(param0);" , "\nthis.append(param0);\n" , "java.awt.TextArea" , "appendText" , "java.lang.String") , anyOf (contains ("append"))) ;
  }

  @Test
  void insertText() throws Exception {
assertThat (synthesiseNeural ("insertText" , "this.insertText(param0, param1);" , "\nthis.insert(param0, param1);\n" , "java.awt.TextArea" , "insertText" , "java.lang.String" , "int") , anyOf (contains ("insert"))) ;
  }

  @Test
  void minimumSize1() throws Exception {
assertThat (synthesiseNeural ("minimumSize1" , "this.minimumSize();" , "\nthis.getMinimumSize();\n" , "java.awt.TextArea" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void minimumSize2() throws Exception {
assertThat (synthesiseNeural ("minimumSize2" , "this.minimumSize(param0, param1);" , "\nthis.getMinimumSize(param0, param1);\n" , "java.awt.TextArea" , "minimumSize" , "int" , "int") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void preferredSize1() throws Exception {
assertThat (synthesiseNeural ("preferredSize1" , "this.preferredSize();" , "\nthis.getPreferredSize();\n" , "java.awt.TextArea" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void preferredSize2() throws Exception {
assertThat (synthesiseNeural ("preferredSize2" , "this.preferredSize(param0, param1);" , "\nthis.getPreferredSize(param0, param1);\n" , "java.awt.TextArea" , "preferredSize" , "int" , "int") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void replaceText() throws Exception {
assertThat (synthesiseNeural ("replaceText" , "this.replaceText(param0, param1, param2);" , "\nthis.replaceRange(param0, param1, param2);\n" , "java.awt.TextArea" , "replaceText" , "java.lang.String" , "int" , "int") , anyOf (contains ("replaceRange"))) ;
  }
}
