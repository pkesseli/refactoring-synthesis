
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_InputVerifierTest {
  @Test
  void shouldYieldFocus() throws Exception {
    assertThat(synthesiseGPT("public class MyInputVerifier extends InputVerifier {\n    @Override\n    public boolean shouldYieldFocus(JComponent input) {\n        return validate(input);\n    }\n}\n\nthis.shouldYieldFocus(a);\n\n", "public class MyInputVerifier extends InputVerifier {\n    @Override\n    public boolean verify(JComponent input) {\n        return validate(input);\n    }\n}\n\nthis.getInputVerifier().verify(a);\n", "javax.swing.InputVerifier", "shouldYieldFocus", "javax.swing.JComponent"), anyOf(contains("shouldYieldFocus")));
  }
}