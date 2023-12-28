
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_TextFieldTest {
  @Test
  void minimumSize1() throws Exception {
assertThat (synthesiseGPT ("minimumSize1" , "this.minimumSize();" , "\nthis.getMinimumSize();\n" , "java.awt.TextField" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void minimumSize2() throws Exception {
assertThat (synthesiseGPT ("minimumSize2" , "this.minimumSize(param0);" , "\nthis.getMinimumSize(param0);\n" , "java.awt.TextField" , "minimumSize" , "int") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void preferredSize1() throws Exception {
assertThat (synthesiseGPT ("preferredSize1" , "this.preferredSize();" , "\nthis.getPreferredSize();\n" , "java.awt.TextField" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void preferredSize2() throws Exception {
assertThat (synthesiseGPT ("preferredSize2" , "this.preferredSize(param0);" , "\nthis.getPreferredSize(param0);\n" , "java.awt.TextField" , "preferredSize" , "int") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void setEchoCharacter() throws Exception {
assertThat (synthesiseGPT ("setEchoCharacter" , "this.setEchoCharacter(param0);" , "\nthis.setEchoChar(param0);\n" , "java.awt.TextField" , "setEchoCharacter" , "char") , anyOf (contains ("setEchoChar"))) ;
  }
}
