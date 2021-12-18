package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/** Implements {@link CounterexampleListener} by writing to the console. */
public class ConsoleCounterexampleListener implements CounterexampleListener {

  /** {@link System#lineSeparator()} */
  private static final String CRLF = System.lineSeparator();

  /** Block indent sequence. */
  private static final String INDENT = "  ";

  @Override
  public void spurious(final Counterexample counterexample) {
    System.out.printf("CE spurious: %s%n", toString(counterexample));
  }

  @Override
  public void genuine(final Counterexample counterexample, final ExecutionResult expected,
      final ExecutionResult actual) {
    System.out.printf("CE genuine: %s, expected %s but was %s.%n", toString(counterexample), toString(expected),
        toString(actual));
  }

  /**
   * Converts an {@link ExecutionResult} to {@link String}.
   * 
   * @param result {@link ExecutionResult} to convert.
   * @return {@link String} representation of {@code result}.
   */
  static String toString(final ExecutionResult result) {
    final StringBuilder stringBuilder = new StringBuilder("{ ");
    if (result.Instance != null) {
      stringBuilder.append("Instance = ");
      stringBuilder.append(result.Instance);
      stringBuilder.append(", ");
    }
    if (result.Error != null) {
      stringBuilder.append("Exception = ");
      stringBuilder.append(result.Error);
    } else {
      stringBuilder.append("Return = ");
      stringBuilder.append(result.Value);
    }
    stringBuilder.append(" }");
    return stringBuilder.toString();
  }

  /**
   * Converts an {@link Counterexample} to {@link String}.
   * 
   * @param result {@link ExecutionCounterexampleResult} to convert.
   * @return {@link String} representation of {@code result}.
   */
  static String toString(final Counterexample counterexample) {
    final StringBuilder stringBuilder = new StringBuilder("Counterexample { ");
    final boolean hasArguments = !counterexample.Arguments.isEmpty();
    final boolean hasFields = !counterexample.Fields.isEmpty();

    if (hasArguments || hasFields) {
      stringBuilder.append(CRLF);
      stringBuilder.append(INDENT);
    }
    stringBuilder.append("Instance = ");
    stringBuilder.append(counterexample.Instance);

    if (hasArguments) {
      stringBuilder.append(CRLF);
      stringBuilder.append(INDENT);
      stringBuilder.append("Arguments = ");
      stringBuilder.append(counterexample.Arguments.toString());
    }
    if (hasFields) {
      stringBuilder.append(CRLF);
      stringBuilder.append(INDENT);
      stringBuilder.append("Fields = ");
      stringBuilder.append(counterexample.Fields.toString());
    }

    if (hasArguments || hasFields)
      stringBuilder.append(CRLF);
    else
      stringBuilder.append(' ');

    stringBuilder.append('}');
    return stringBuilder.toString();
  }

}
