
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_swing_JViewportTest {
  @Test
  void isBackingStoreEnabled() throws Exception {
    assertThat(synthesiseGPT("boolean isBackingStoreEnabled = this.isBackingStoreEnabled();\n\n", "boolean isBackingStoreEnabled = this.getScrollMode() == JViewport.BACKINGSTORE_SCROLL_MODE;\n", "javax.swing.JViewport", "isBackingStoreEnabled"), anyOf(contains("getScrollMode")));
  }

  @Test
  void setBackingStoreEnabled() throws Exception {
    assertThat(synthesiseGPT("this.setBackingStoreEnabled(a);\n", "this.setScrollMode(a ? JViewport.BACKINGSTORE_SCROLL_MODE : JViewport.BLIT_SCROLL_MODE);\n", "javax.swing.JViewport", "setBackingStoreEnabled", "boolean"), anyOf(contains("setScrollMode")));
  }
}