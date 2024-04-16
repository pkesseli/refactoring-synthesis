
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ScrollbarTest {
  @Test
  void getLineIncrement() throws Exception {
assertThat (synthesiseNeural ("getLineIncrement" , "this.getLineIncrement();" , "\nthis.getUnitIncrement();\n" , "java.awt.Scrollbar" , "getLineIncrement") , anyOf (contains ("getUnitIncrement"))) ;
  }

  @Test
  void getPageIncrement() throws Exception {
assertThat (synthesiseNeural ("getPageIncrement" , "this.getPageIncrement();" , "\nthis.getBlockIncrement();\n" , "java.awt.Scrollbar" , "getPageIncrement") , anyOf (contains ("getBlockIncrement"))) ;
  }

  @Test
  void getVisible() throws Exception {
assertThat (synthesiseNeural ("getVisible" , "this.getVisible();" , "\nthis.getVisibleAmount();\n" , "java.awt.Scrollbar" , "getVisible") , anyOf (contains ("getVisibleAmount"))) ;
  }

  @Test
  void setLineIncrement() throws Exception {
assertThat (synthesiseNeural ("setLineIncrement" , "this.setLineIncrement(param0);" , "\nthis.setUnitIncrement(param0);\n" , "java.awt.Scrollbar" , "setLineIncrement" , "int") , anyOf (contains ("setUnitIncrement"))) ;
  }

  @Test
  void setPageIncrement() throws Exception {
assertThat (synthesiseNeural ("setPageIncrement" , "this.setPageIncrement(param0);" , "\nthis.setBlockIncrement(param0);\n" , "java.awt.Scrollbar" , "setPageIncrement" , "int") , anyOf (contains ("setBlockIncrement"))) ;
  }
}
