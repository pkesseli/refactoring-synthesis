
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ContainerTest {
  @Test
  void countComponents() throws Exception {
assertThat (synthesiseNeural ("countComponents" , "this.countComponents();" , "\nthis.getComponentCount();\n" , "java.awt.Container" , "countComponents") , anyOf (contains ("getComponentCount"))) ;
  }

  @Test
  void deliverEvent() throws Exception {
assertThat (synthesiseNeural ("deliverEvent" , "this.deliverEvent(param0);" , "\nthis.dispatchEvent(param0);\n" , "java.awt.Container" , "deliverEvent" , "java.awt.Event") , anyOf (contains ("dispatchEvent"))) ;
  }

  @Test
  void insets() throws Exception {
assertThat (synthesiseNeural ("insets" , "this.insets();" , "\nthis.getInsets();\n" , "java.awt.Container" , "insets") , anyOf (contains ("getInsets"))) ;
  }

  @Test
  void layout() throws Exception {
assertThat (synthesiseNeural ("layout" , "this.layout();" , "\nthis.doLayout();\n" , "java.awt.Container" , "layout") , anyOf (contains ("doLayout"))) ;
  }

  @Test
  void locate() throws Exception {
assertThat (synthesiseNeural ("locate" , "this.locate(param0, param1);" , "\nthis.getComponentAt(param0, param1);\n" , "java.awt.Container" , "locate" , "int" , "int") , anyOf (contains ("getComponentAt"))) ;
  }

  @Test
  void minimumSize() throws Exception {
assertThat (synthesiseNeural ("minimumSize" , "this.minimumSize();" , "\nthis.getMinimumSize();\n" , "java.awt.Container" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void preferredSize() throws Exception {
assertThat (synthesiseNeural ("preferredSize" , "this.preferredSize();" , "\nthis.getPreferredSize();\n" , "java.awt.Container" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }
}
