package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;

/**
 * Null implementation for {@link InstructionSetSeed}.
 */
public class NullSeed implements InstructionSetSeed {

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
  }

}
