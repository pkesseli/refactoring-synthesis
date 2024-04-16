
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class javax_swing_plaf_basic_BasicSplitPaneUITest {
  @Disabled("No replacement")
  @Test
  void createKeyboardDownRightListener() throws Exception {
assertThat (synthesiseNeural ("createKeyboardDownRightListener" , "this.createKeyboardDownRightListener();" , "\n// The method is deprecated and no longer needed.\n// Refactor the code to remove the method call.\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardDownRightListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardEndListener() throws Exception {
assertThat (synthesiseNeural ("createKeyboardEndListener" , "this.createKeyboardEndListener();" , "\n// The method is deprecated and no longer needed.\n// You can safely remove this line of code.\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardEndListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardHomeListener() throws Exception {
assertThat (synthesiseNeural ("createKeyboardHomeListener" , "this.createKeyboardHomeListener();" , "\n// Deprecated method call\nthis.createKeyboardHomeListener();\n\n// Refactored code\n// As per the deprecation comment, this method is no longer needed and can be safely removed.\n// There is no replacement method mentioned in the deprecation comment.\n// So, we can simply remove this method call.\n" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardHomeListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardResizeToggleListener() throws Exception {
assertThat (synthesiseNeural ("createKeyboardResizeToggleListener" , "this.createKeyboardResizeToggleListener();" , "\n// The method is deprecated and no longer needed.\n// You can safely remove this method call.\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardResizeToggleListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void createKeyboardUpLeftListener() throws Exception {
assertThat (synthesiseNeural ("createKeyboardUpLeftListener" , "this.createKeyboardUpLeftListener();" , "\n// The method is deprecated and no longer exists in Java 2 platform v1.3\n// You can remove this method call or replace it with an alternative implementation\n;" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "createKeyboardUpLeftListener") , Matchers . anything ()) ;
  }

  @Disabled("No replacement")
  @Test
  void getDividerBorderSize() throws Exception {
assertThat (synthesiseNeural ("getDividerBorderSize" , "this.getDividerBorderSize();" , "" , "javax.swing.plaf.basic.BasicSplitPaneUI" , "getDividerBorderSize") , Matchers . anything ()) ;
  }
}
