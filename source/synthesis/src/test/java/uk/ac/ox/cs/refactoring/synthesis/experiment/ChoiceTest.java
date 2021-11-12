package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.testAlias;

import org.junit.jupiter.api.Test;

public class ChoiceTest {
  @Test
  void countItems() throws Exception {
    testAlias(".getItemCount(", "java.awt.Choice", "countItems");
  }
}
