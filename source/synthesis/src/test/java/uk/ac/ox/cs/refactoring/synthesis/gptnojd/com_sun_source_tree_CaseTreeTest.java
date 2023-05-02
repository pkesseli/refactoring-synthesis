
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class com_sun_source_tree_CaseTreeTest {
  @Test
  void getExpression() throws Exception {
    assertThat(synthesiseGPT("CaseTree caseTree = ...;\nExpressionTree expressionTree = caseTree.getExpression();\n\n", "CaseTree caseTree = ...;\nExpressionTree expressionTree = caseTree.getExpression();\n", "com.sun.source.tree.CaseTree", "getExpression"), anyOf(contains("getExpressions")));
  }
}