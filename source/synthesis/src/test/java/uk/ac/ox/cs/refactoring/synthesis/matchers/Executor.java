package uk.ac.ox.cs.refactoring.synthesis.matchers;

import java.lang.reflect.InvocationTargetException;

import org.opentest4j.AssertionFailedError;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.BlockStatement;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

class Executor {

  final State Input;

  Executor(final Object instance, final Object... arguments) {
    Input = new State(instance, arguments);
  }

  Object run(final SnippetCandidate candidate) {
    final ExecutionContext context = new ExecutionContext(MapsToValue.class.getClassLoader(), Input);
    final BlockStatement statement = candidate.Block;
    try {
      return statement.execute(context);
    } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException
        | NoSuchFieldException | NoSuchMethodException e) {
      throw new AssertionFailedError("Unexpected exception in candidate execution", e);
    }
  }
}
