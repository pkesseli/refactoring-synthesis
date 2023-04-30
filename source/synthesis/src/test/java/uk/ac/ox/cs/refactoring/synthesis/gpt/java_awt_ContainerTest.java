
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ContainerTest {
  @Test
  void countComponents() throws Exception {
    assertThat(synthesiseGPT("<code before refactoring here>\nint numComponents = this.countComponents();\n<code after refactoring here>\nint numComponents = this.getComponentCount();\n", "", "java.awt.Container", "countComponents"), Matchers.anything());
  }

  @Test
  void deliverEvent() throws Exception {
    assertThat(synthesiseGPT("this.deliverEvent(a);\n", "this.dispatchEvent(a);\n", "java.awt.Container", "deliverEvent", "java.awt.Event"), anyOf(contains("dispatchEvent")));
  }

  @Test
  void insets() throws Exception {
    assertThat(synthesiseGPT("Insets insets = this.insets();\n\n", "Insets insets = this.getInsets();\n", "java.awt.Container", "insets"), anyOf(contains("getInsets")));
  }

  @Test
  void layout() throws Exception {
    assertThat(synthesiseGPT("this.layout();\n\n", "this.doLayout();\n", "java.awt.Container", "layout"), anyOf(contains("doLayout")));
  }

  @Test
  void locate() throws Exception {
    assertThat(synthesiseGPT("Component component = this.locate(a, b);\n\n", "Component component = this.getComponentAt(a, b);\n", "java.awt.Container", "locate", "int", "int"), anyOf(contains("getComponentAt")));
  }

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize();\n\n", "this.getMinimumSize();\n", "java.awt.Container", "minimumSize"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseGPT("Dimension preferredSize = this.preferredSize();\n\n", "Dimension preferredSize = this.getPreferredSize();\n", "java.awt.Container", "preferredSize"), anyOf(contains("getPreferredSize")));
  }
}