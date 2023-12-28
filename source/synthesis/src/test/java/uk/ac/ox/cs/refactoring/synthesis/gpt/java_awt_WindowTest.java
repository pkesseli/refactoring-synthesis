
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_WindowTest {
  @Test
  void applyResourceBundle1() throws Exception {
assertThat (synthesiseGPT ("applyResourceBundle1" , "this.applyResourceBundle(param0);" , "\nthis.applyComponentOrientation(ComponentOrientation.getOrientation(ResourceBundle.getBundle(param0).getLocale()));\n" , "java.awt.Window" , "applyResourceBundle" , "java.lang.String") , anyOf (contains ("applyComponentOrientation"))) ;
  }

  @Test
  void applyResourceBundle2() throws Exception {
assertThat (synthesiseGPT ("applyResourceBundle2" , "this.applyResourceBundle(param0);" , "\nthis.applyComponentOrientation(ComponentOrientation.getOrientation(param0.getLocale()));\n" , "java.awt.Window" , "applyResourceBundle" , "java.util.ResourceBundle") , anyOf (contains ("applyComponentOrientation"))) ;
  }

  @Test
  void hide() throws Exception {
assertThat (synthesiseGPT ("hide" , "this.hide();" , "\nthis.setVisible(false);\n" , "java.awt.Window" , "hide") , anyOf (contains ("setVisible"))) ;
  }

  @Test
  void postEvent() throws Exception {
assertThat (synthesiseGPT ("postEvent" , "this.postEvent(param0);" , "\nthis.dispatchEvent(param0);\n" , "java.awt.Window" , "postEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }

  @Test
  void reshape() throws Exception {
assertThat (synthesiseGPT ("reshape" , "this.reshape(param0, param1, param2, param3);" , "\nthis.setBounds(param0, param1, param2, param3);\n" , "java.awt.Window" , "reshape" , "int" , "int" , "int" , "int") , anyOf (contains ("setBounds"))) ;
  }

  @Test
  void show() throws Exception {
assertThat (synthesiseGPT ("show" , "this.show();" , "\nthis.setVisible(true);\n" , "java.awt.Window" , "show") , anyOf (contains ("setVisible"))) ;
  }
}
