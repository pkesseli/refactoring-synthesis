package uk.ac.ox.cs.refactoring.synthesis.state;

import org.junit.jupiter.api.Test;

public class ObjenesisStateFactoryTest {
  private final IStateFactory stateFactory = new ObjenesisStateFactory();

  @Test
  void createInstanceNoArgumentsNoFields() {
  }
}
