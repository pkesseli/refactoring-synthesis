
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.testAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ComponentTest {
  @Test
  void bounds() throws Exception {
    testAlias(".getBounds(", "java.awt.Component", "bounds");
  }

  @Test
  @Disabled("Method signatures differ, can't find conversion with minimal grammar.")
  void deliverEvents() throws Exception {
    testAlias(".dispatchEvent(", "java.awt.Component", "deliverEvent", "java.awt.Event");
  }

  @Test
  void disable() throws Exception {
    testAlias(".setEnabled(", "java.awt.Component", "disable");
  }

  @Test
  void enable() throws Exception {
    // TODO: Unsound result!
    testAlias(".setEnabled(", "java.awt.Component", "enable");
  }
}
