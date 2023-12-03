
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JListTest {
  @Test
  void getSelectedValues() throws Exception {
assertThat (synthesiseGPT ("this.getSelectedValues();" , "\nTextUI ui = this.getUI();\nPoint2D point = new Point2D.Float(0, 0);\nint[] selectionIndices = this.getSelectedIndices();\nPosition.Bias[] bias = new Position.Bias[selectionIndices.length];\nfor (int i = 0; i < selectionIndices.length; i++) {\n    bias[i] = Position.Bias.Forward;\n}\nfor (int i = 0; i < selectionIndices.length; i++) {\n    Rectangle rect = this.getCellBounds(selectionIndices[i], selectionIndices[i]);\n    point.setLocation(rect.x, rect.y);\n    int index = ui.viewToModel2D(this, point, bias);\n    Object value = this.getModel().getElementAt(index);\n    // do something with value\n}\n;" , "javax.swing.JList" , "getSelectedValues") , anyOf (contains ("getSelectedValuesList"))) ;
  }
}
