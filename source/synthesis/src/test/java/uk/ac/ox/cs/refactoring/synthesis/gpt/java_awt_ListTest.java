
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
assertThat (synthesiseGPT ("addItem1" , "this.addItem(param0);" , "\nthis.add(param0);\n" , "java.awt.List" , "addItem" , "java.lang.String") , anyOf (contains ("add"))) ;
  }

  @Test
  void addItem2() throws Exception {
assertThat (synthesiseGPT ("addItem2" , "this.addItem(param0, param1);" , "\nthis.add(param0, param1);\n" , "java.awt.List" , "addItem" , "java.lang.String" , "int") , anyOf (contains ("add"))) ;
  }

  @Test
  void allowsMultipleSelections() throws Exception {
assertThat (synthesiseGPT ("allowsMultipleSelections" , "this.allowsMultipleSelections();" , "\nthis.isMultipleMode()\n;" , "java.awt.List" , "allowsMultipleSelections") , anyOf (contains ("isMultipleMode"))) ;
  }

  @Test
  void clear() throws Exception {
assertThat (synthesiseGPT ("clear" , "this.clear();" , "\nfor (int i = this.getItemCount() - 1; i >= 0; i--) {\n    this.remove(i);\n}\n" , "java.awt.List" , "clear") , anyOf (contains ("removeAll"))) ;
  }

  @Test
  void countItems() throws Exception {
assertThat (synthesiseGPT ("countItems" , "this.countItems();" , "this.getItemCount();" , "java.awt.List" , "countItems") , anyOf (contains ("getItemCount"))) ;
  }

  @Test
  void delItem() throws Exception {
assertThat (synthesiseGPT ("delItem" , "this.delItem(param0);" , "\nthis.remove(param0);\n" , "java.awt.List" , "delItem" , "int") , anyOf (contains ("remove"))) ;
  }

  @Test
  void isSelected() throws Exception {
assertThat (synthesiseGPT ("isSelected" , "this.isSelected(param0);" , "\nthis.isIndexSelected(param0)\n;" , "java.awt.List" , "isSelected" , "int") , anyOf (contains ("isIndexSelected"))) ;
  }

  @Test
  void minimumSize1() throws Exception {
assertThat (synthesiseGPT ("minimumSize1" , "this.minimumSize();" , "\nthis.getMinimumSize(this.getItemCount())\n;" , "java.awt.List" , "minimumSize") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void minimumSize2() throws Exception {
assertThat (synthesiseGPT ("minimumSize2" , "this.minimumSize(param0);" , "\nthis.getPreferredSize()\n;" , "java.awt.List" , "minimumSize" , "int") , anyOf (contains ("getMinimumSize"))) ;
  }

  @Test
  void preferredSize1() throws Exception {
assertThat (synthesiseGPT ("preferredSize1" , "this.preferredSize();" , "\nthis.getPreferredSize()\n;" , "java.awt.List" , "preferredSize") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void preferredSize2() throws Exception {
assertThat (synthesiseGPT ("preferredSize2" , "this.preferredSize(param0);" , "\nthis.getPreferredSize();\n" , "java.awt.List" , "preferredSize" , "int") , anyOf (contains ("getPreferredSize"))) ;
  }

  @Test
  void setMultipleSelections() throws Exception {
assertThat (synthesiseGPT ("setMultipleSelections" , "this.setMultipleSelections(param0);" , "\nthis.setMultipleMode(param0);\n" , "java.awt.List" , "setMultipleSelections" , "boolean") , anyOf (contains ("setMultipleMode"))) ;
  }
}
