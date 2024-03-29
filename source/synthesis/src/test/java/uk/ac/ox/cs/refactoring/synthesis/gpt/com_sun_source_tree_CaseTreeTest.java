
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class com_sun_source_tree_CaseTreeTest {
  @Test
  void getExpression() throws Exception {
assertThat (synthesiseGPT ("getExpression" , "this.getExpression();" , "\nthis.getExpressions().get(0)\n;" , "com.sun.source.tree.CaseTree" , "getExpression") , anyOf (contains ("getExpressions"))) ;
  }
}
