
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_ListTest {
  @Test
  void addItem1() throws Exception {
    assertThat(synthesiseGPT("this.addItem(a);\n", "this.add(a);\n", "java.awt.List", "addItem", "java.lang.String"), anyOf(contains("add")));
  }

  @Test
  void addItem2() throws Exception {
    assertThat(synthesiseGPT("this.addItem(a, b);\n\n", "this.add(a, b);\n", "java.awt.List", "addItem", "java.lang.String", "int"), anyOf(contains("add")));
  }

  @Test
  void allowsMultipleSelections() throws Exception {
    assertThat(synthesiseGPT("boolean allowMultipleSelections = this.allowsMultipleSelections();\n\n", "boolean allowMultipleSelections = this.isMultipleMode();\n", "java.awt.List", "allowsMultipleSelections"), anyOf(contains("isMultipleMode")));
  }

  @Test
  void clear() throws Exception {
    assertThat(synthesiseGPT("this.clear();\n\n", "this.removeAll();\n", "java.awt.List", "clear"), anyOf(contains("removeAll")));
  }

  @Test
  void countItems() throws Exception {
    assertThat(synthesiseGPT("int itemCount = this.countItems();\n\n", "int itemCount = this.getItemCount();\n", "java.awt.List", "countItems"), anyOf(contains("getItemCount")));
  }

  @Test
  void delItem() throws Exception {
    assertThat(synthesiseGPT("<code before refactoring here>\nthis.delItem(a);\n<code after refactoring here>\nthis.remove(a);\n", "", "java.awt.List", "delItem", "int"), anyOf(contains("remove")));
  }

  @Test
  void delItems() throws Exception {
    assertThat(synthesiseGPT("this.delItems(a, b);\n", "int[] selectedIndices = this.getSelectedIndexes();\nList<String> items = new ArrayList<>();\nfor (int i = 0; i < selectedIndices.length; i++) {\n    if (selectedIndices[i] < a || selectedIndices[i] > b) {\n        items.add(this.getItem(selectedIndices[i]));\n    }\n}\nthis.removeAll();\nfor (String item : items) {\n    this.add(item);\n}\n", "java.awt.List", "delItems", "int", "int"), Matchers.anything());
  }

  @Test
  void isSelected() throws Exception {
    assertThat(synthesiseGPT("<code before refactoring here>\nthis.isSelected(a)\n<code after refactoring here>\nthis.isIndexSelected(a)\n", "", "java.awt.List", "isSelected", "int"), anyOf(contains("isIndexSelected")));
  }

  @Test
  void minimumSize1() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize();\n\n", "this.getMinimumSize();\n", "java.awt.List", "minimumSize"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void minimumSize2() throws Exception {
    assertThat(synthesiseGPT("this.minimumSize(a);\n\n", "this.getMinimumSize(a);\n", "java.awt.List", "minimumSize", "int"), anyOf(contains("getMinimumSize")));
  }

  @Test
  void preferredSize1() throws Exception {
    assertThat(synthesiseGPT("Dimension preferredSize = this.preferredSize();\n\n", "Dimension preferredSize = this.getPreferredSize();\n", "java.awt.List", "preferredSize"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void preferredSize2() throws Exception {
    assertThat(synthesiseGPT("this.preferredSize(a);\n\n", "this.getPreferredSize(a);\n", "java.awt.List", "preferredSize", "int"), anyOf(contains("getPreferredSize")));
  }

  @Test
  void setMultipleSelections() throws Exception {
    assertThat(synthesiseGPT("this.setMultipleSelections(a);\n", "this.setMultipleMode(a);\n", "java.awt.List", "setMultipleSelections", "boolean"), anyOf(contains("setMultipleMode")));
  }
}