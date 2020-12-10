package uk.ac.ox.cs.refactoring.synthesis.state;

/**
 * Invocation state of a Java method. Since a method always needs to be invoked
 * in the context of a {@link ClassLoader}, static fields are not explicitly
 * modelled here.
 */
public class State {
  /**
   * Instance on which to invoke the Java method. <code>null</code> for static
   * methods.
   */
  public final Object Instance;

  /**
   * Arguments using which to invoke the Java method.
   */
  public final Object[] Arguments;

  /**
   * @param instance  {@link #Instance}
   * @param arguments {@link #Arguments}
   */
  public State(Object instance, Object[] arguments) {
    Instance = instance;
    Arguments = arguments;
  }
}
