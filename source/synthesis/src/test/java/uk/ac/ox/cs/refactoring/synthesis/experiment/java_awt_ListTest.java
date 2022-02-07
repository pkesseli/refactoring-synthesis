package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAliasBenchmark;

import org.junit.jupiter.api.Test;

class java_awt_ListTest {

  @Test
  void addItem() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "addItem", "java.lang.String"), contains(".add("));
  }

  @Test
  void addItemStringInt() throws Exception {
    assertThat(synthesiseAliasBenchmark("addItemStringInt", "java.awt.List", "addItem", "java.lang.String", "int"),
        contains(".add("));
  }

  @Test
  void allowsMultipleSelections() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "allowsMultipleSelections"), contains(".isMultipleMode("));
  }

  @Test
  void clear() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "clear"), contains(".removeAll("));
  }

  @Test
  void countItems() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "countItems"), contains(".getItemCount("));
  }

  @Test
  void delItem() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "delItem", "int"), contains(".remove("));
  }

  @Test
  void isSelected() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "isSelected", "int"), contains(".isIndexSelected("));
  }

  @Test
  void minimumSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "minimumSize"), contains(".getMinimumSize("));
  }

  @Test
  void minimumSizeInt() throws Exception {
    assertThat(synthesiseAliasBenchmark("minimumSizeInt", "java.awt.List", "minimumSize", "int"),
        contains(".getMinimumSize("));
  }

  @Test
  void preferredSize() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "preferredSize"), contains(".getPreferredSize("));
  }

  @Test
  void preferredSizeInt() throws Exception {
    assertThat(synthesiseAliasBenchmark("preferredSizeInt", "java.awt.List", "preferredSize", "int"),
        contains(".getPreferredSize("));
  }

  @Test
  void setMultipleSelections() throws Exception {
    assertThat(synthesiseAlias("java.awt.List", "setMultipleSelections", "boolean"), contains(".setMultipleMode("));
  }
}
