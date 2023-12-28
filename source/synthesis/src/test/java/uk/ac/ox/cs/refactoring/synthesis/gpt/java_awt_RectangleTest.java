
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_RectangleTest {
  @Test
  void inside() throws Exception {
assertThat (synthesiseGPT ("inside" , "this.inside(param0, param1);" , "\nthis.contains(param0, param1);\n" , "java.awt.Rectangle" , "inside" , "int" , "int") , anyOf (contains ("contains"))) ;
  }

  @Test
  void move() throws Exception {
assertThat (synthesiseGPT ("move" , "this.move(param0, param1);" , "\nthis.setLocation(param0, param1);\n" , "java.awt.Rectangle" , "move" , "int" , "int") , anyOf (contains ("setLocation"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("reshape" , "this.reshape(param0, param1, param2, param3);" , "\nthis.setBounds(param0, param1, param2, param3);\n" , "java.awt.Rectangle" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void resize() throws Exception {
assertThat (synthesiseGPT ("resize" , "this.resize(param0, param1);" , "\nthis.setSize(param0, param1);\n" , "java.awt.Rectangle" , "resize" , "int" , "int") , anyOf (contains ("setSize"))) ;
  }
}
