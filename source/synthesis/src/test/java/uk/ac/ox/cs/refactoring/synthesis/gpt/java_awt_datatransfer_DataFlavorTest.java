
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_awt_datatransfer_DataFlavorTest {
  @Test
  void equals() throws Exception {
assertThat (synthesiseGPT ("this.equals(param0);" , "\nDataFlavor.stringFlavor.getMimeType().equals(param0)\n;" , "java.awt.datatransfer.DataFlavor" , "equals" , "java.lang.String") , anyOf (contains ("hashCode") , contains ("isMimeTypeEqual"))) ;
  }
}
