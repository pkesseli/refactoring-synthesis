package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;

/**
 * Implementing classes add instructions to instruction sets.
 */
public interface InstructionSetSeed {
  /**
   * Extends the given instruction set.
   * 
   * @param components Instruction set to extend.
   * @throws ClassNotFoundException if a reflective class access fails during
   *                                analysis.
   * @throws NoSuchMethodException  if a reflective method access fails during
   *                                analysis.
   */
  void seed(ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException;
}
