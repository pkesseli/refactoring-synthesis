
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class com_sun_source_tree_CaseTreeTest {
  // Is it counted as a change of return type?
  @Test
  void getExpression() throws Exception {
    assertThat(synthesiseAlias("com.sun.source.tree.CaseTree", "getExpression"), anyOf(contains("getExpressions")));
  }
}