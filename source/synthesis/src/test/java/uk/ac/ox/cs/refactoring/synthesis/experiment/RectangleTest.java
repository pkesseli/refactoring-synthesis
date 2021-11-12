package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.testAlias;

import org.junit.jupiter.api.Test;

public class RectangleTest {
  @Test
  void inside() throws Exception {
    testAlias(".contains(", "java.awt.Rectangle", "inside", "int", "int");
  }

  @Test
  void move() throws Exception {
    testAlias(".setLocation(", "java.awt.Rectangle", "move", "int", "int");
  }

  @Test
  void reshape() throws Exception {
    testAlias(".setBounds(", "java.awt.Rectangle", "reshape", "int", "int", "int", "int");
  }

  @Test
  void resize() throws Exception {
    testAlias(".setSize(", "java.awt.Rectangle", "resize", "int", "int");
  }
}
