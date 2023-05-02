
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ContainerTest {
  @Test
  void countComponents() throws Exception {
    assertThat(synthesiseGPT("int componentCount = this.countComponents();\n\n", "int componentCount = this.getComponentCount();\n", "java.awt.Container", "countComponents"), Matchers.anything());
  }

  @Test
  void deliverEvent() throws Exception {
    assertThat(synthesiseGPT("this.deliverEvent(a);\n", "Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(a);\n", "java.awt.Container", "deliverEvent", "java.awt.Event"), anyOf(contains("dispatchEvent")));
  }

  @Test
  void insets() throws Exception {
    assertThat(synthesiseGPT("Insets insets = this.insets();\nint top = insets.top;\nint left = insets.left;\nint bottom = insets.bottom;\nint right = insets.right;\n\n", "Insets insets = getInsets();\nint top = insets.top;\nint left = insets.left;\nint bottom = insets.bottom;\nint right = insets.right;\n", "java.awt.Container", "insets"), anyOf(contains("getInsets")));
  }

  @Test
  void layout() throws Exception {
    assertThat(synthesiseGPT("this.layout();\n\n", "this.doLayout();\n", "java.awt.Container", "layout"), anyOf(contains("doLayout")));
  }

  @Test
  void locate() throws Exception {
    assertThat(synthesiseGPT("this.locate(a, b);\n\n", "SwingUtilities.convertPointToScreen(new Point(0, 0), this).getLocation();\n", "java.awt.Container", "locate", "int", "int"), anyOf(contains("getComponentAt")));
  }

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseGPT("Dimension minimumSize = this.minimumSize();\n\n", "Dimension minimumSize = this.getMinimumSize();\n", "java.awt.Container", "minimumSize"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseGPT("Dimension preferredSize = this.preferredSize();\n\n", "Dimension preferredSize = this.getPreferredSize();\n", "java.awt.Container", "preferredSize"), anyOf(contains("getPreferredSize")));
  }
}