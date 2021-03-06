package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class SnippetCandidateExecutor implements CandidateExecutor<SnippetCandidate> {

  private final IStateFactory stateFactory;

  public SnippetCandidateExecutor(final IStateFactory stateFactory) {
    this.stateFactory = stateFactory;
  }

  @Override
  public ExecutionResult execute(final SnippetCandidate candidate, final Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final IsolatedClassLoader classLoader = ClassLoaders.createIsolated();
    final State state = stateFactory.create(classLoader, counterexample);
    final ExecutionContext context = new ExecutionContext(classLoader, state);
    final Object value;
    try {
      value = candidate.Block.execute(context);
    } catch (final Exception e) {
      return new ExecutionResult(classLoader, e);
    }
    return new ExecutionResult(classLoader, value);
  }

}
