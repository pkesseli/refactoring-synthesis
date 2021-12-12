package uk.ac.ox.cs.refactoring.synthesis.counterexample;

/**
 * Stores context during {@link CounterexampleGenerator} run, e.g.
 * {@link #ObjectCount}.
 * 
 * TODO: Replace this by a limit on the counterexample depth.
 */
class GenerationContext {

  /** Indicates how many objects were already generated in the counterexample. */
  int ObjectCount;
}
