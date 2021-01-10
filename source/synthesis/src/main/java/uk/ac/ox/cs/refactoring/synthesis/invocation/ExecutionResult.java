package uk.ac.ox.cs.refactoring.synthesis.invocation;

/**
 * Result of a method invocation, either exceptional or a return value.
 */
public final class ExecutionResult {
  /**
   * <code>null</code> if method invocation completed successfully, otherwise the
   * thrown exception.
   */
  public final Throwable Error;

  /**
   * <code>null</code> if method invocation completed exceptionally or the invoked
   * method is a void method, otherwise the returned value.
   */
  public final Object Value;

  /**
   * @param error {@lin #Error}
   */
  public ExecutionResult(final Throwable error) {
    this(error, null);
  }

  /**
   * @param value {@link #Value}
   */
  public ExecutionResult(final Object value) {
    this(null, value);
  }

  /**
   * @param error {@link #Error}
   * @param value {@link #Value}
   */
  private ExecutionResult(final Throwable error, final Object value) {
    Error = error;
    Value = value;
  }
}
