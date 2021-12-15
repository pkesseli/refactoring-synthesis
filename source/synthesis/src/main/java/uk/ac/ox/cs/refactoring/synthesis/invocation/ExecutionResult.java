package uk.ac.ox.cs.refactoring.synthesis.invocation;

import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;

/**
 * Result of a method invocation, either exceptional or a return value.
 * 
 * TODO: Also consider {@code this} instance.
 */
public final class ExecutionResult {

  /**
   * Instance on which the method was invoked. {@code null} for static methods.
   */
  public final Object Instance;

  /**
   * Class loader in which the execution was performed.
   */
  public final IsolatedClassLoader ClassLoader;

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
   * @param classLoader {@link #ClassLoader}
   * @param instance    {@link #Instance}
   * @param error       {@lin #Error}
   */
  public ExecutionResult(final IsolatedClassLoader classLoader, final Object instance, final Throwable error) {
    this(classLoader, instance, error, null);
  }

  /**
   * @param classLoader {@link #ClassLoader}
   * @param instance    {@link #Instance}
   * @param value       {@link #Value}
   */
  public ExecutionResult(final IsolatedClassLoader classLoader, final Object instance, final Object value) {
    this(classLoader, instance, null, value);
  }

  /**
   * @param classLoader {@link #ClassLoader}
   * @param instance    {@link #Instance}
   * @param error       {@link #Error}
   * @param value       {@link #Value}
   */
  private ExecutionResult(final IsolatedClassLoader classLoader, final Object instance, final Throwable error,
      final Object value) {
    ClassLoader = classLoader;
    Instance = instance;
    Error = error;
    Value = value;
  }
}
