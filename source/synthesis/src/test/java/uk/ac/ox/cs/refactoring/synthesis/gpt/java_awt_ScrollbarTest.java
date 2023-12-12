
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ScrollbarTest {
  @Test
  void getLineIncrement() throws Exception {
assertThat (synthesiseGPT ("getLineIncrement" , "this.getLineIncrement();" , "\nthis.getBlockIncrement()\n;" , "java.awt.Scrollbar" , "getLineIncrement") , anyOf (contains ("getUnitIncrement"))) ;
  }

  @Test
  void getPageIncrement() throws Exception {
assertThat (synthesiseGPT ("getPageIncrement" , "this.getPageIncrement();" , "\nthis.getVisibleAmount()\n;" , "java.awt.Scrollbar" , "getPageIncrement") , anyOf (contains ("getBlockIncrement"))) ;
  }

  @Test
  void getVisible() throws Exception {
assertThat (synthesiseGPT ("getVisible" , "this.getVisible();" , "\nthis.isVisible()\n;" , "java.awt.Scrollbar" , "getVisible") , anyOf (contains ("getVisibleAmount"))) ;
  }

  @Test
  void setLineIncrement() throws Exception {
assertThat (synthesiseGPT ("setLineIncrement" , "this.setLineIncrement(param0);" , "\nthis.setBlockIncrement(param0);\n" , "java.awt.Scrollbar" , "setLineIncrement" , "int") , anyOf (contains ("setUnitIncrement"))) ;
  }

  @Test
  void setPageIncrement() throws Exception {
assertThat (synthesiseGPT ("setPageIncrement" , "this.setPageIncrement(param0);" , "" , "java.awt.Scrollbar" , "setPageIncrement" , "int") , anyOf (contains ("setBlockIncrement"))) ;
  }
}
