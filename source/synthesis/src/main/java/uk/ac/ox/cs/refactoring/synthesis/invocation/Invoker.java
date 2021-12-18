package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.InvocationTargetException;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.state.ClassLoaderClonerStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;
import uk.ac.ox.cs.refactoring.synthesis.state.StateFactory;

/**
 * Helper to reflectively invoke a Java method based on a counterexample.
 */
public class Invoker {

  /**
   * Name of constructor methods.
   */
  public static final String INIT = "<init>";

  /**
   * Classloader-independent method identifier.
   */
  private final MethodIdentifier methodIdentifier;

  /**
   * Helper to prepare a Java program state based on a given counterexample.
   */
  private final StateFactory stateFactory = new ClassLoaderClonerStateFactory();

  /**
   * @param methodIdentifier {@link #methodIdentifier}
   */
  public Invoker(final MethodIdentifier methodIdentifier) {
    this.methodIdentifier = methodIdentifier;
  }

  /**
   * Invokes the configured method using the given counterexample in the provided
   * {@link ClassLoader}.
   * 
   * @param counterexample {@link Counterexample} modelling the state in which to
   *                       invoke the configured method.
   */
  public ExecutionResult invoke(final Counterexample counterexample)
      throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException,
      NoSuchFieldException, NoSuchMethodException {
    final IsolatedClassLoader classLoader = ClassLoaders.createIsolated();
    final State state = stateFactory.create(classLoader, counterexample);
    final Invokable invokable = Methods.create(classLoader, methodIdentifier);
    final Object result;
    try {
      result = invokable.invoke(state.Instance, state.Arguments);
    } catch (final InvocationTargetException e) {
      return new ExecutionResult(classLoader, state.Instance, e.getCause());
    } catch (final Throwable e) {
      return new ExecutionResult(classLoader, state.Instance, e);
    }
    return new ExecutionResult(classLoader, state.Instance, result);
  }

}
