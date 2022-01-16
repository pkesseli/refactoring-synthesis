package uk.ac.ox.cs.refactoring.synthesis.statistics;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.CounterexampleListener;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

class CountingCounterexampleListener implements CounterexampleListener {

  int Spurious;

  int Genuine;

  @Override
  public void spurious(final Counterexample counterexample) {
    ++Spurious;
  }

  @Override
  public void genuine(final Counterexample counterexample, final ExecutionResult expected,
      final ExecutionResult actual) {
    ++Genuine;
  }
}
