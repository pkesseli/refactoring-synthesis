package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import java.util.Map;
import java.util.function.Function;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeInvokable;

/**
 * Wraps an {@link InvokeFactory} with bound arguments.
 */
public class BoundInvokableFactory<T extends InvokeInvokable> implements Function<Object[], T> {

  /**
   * Constructs the actual method or constructor.
   */
  private final InvokeFactory<T> invokeFactory;

  /**
   * Bound argument expressions.
   */
  private final Map<Integer, IExpression> boundArguments;

  /**
   * @param invokeFactory  {@link #invokeFactory}
   * @param boundArguments {@link #boundArguments}
   */
  public BoundInvokableFactory(final InvokeFactory<T> invokeFactory, final Map<Integer, IExpression> boundArguments) {
    this.invokeFactory = invokeFactory;
    this.boundArguments = boundArguments;
  }

  @Override
  public T apply(final Object[] t) {
    final int numberOfArguments = boundArguments.size() + t.length;
    final Object[] arguments = new Object[numberOfArguments];
    int numberOfBoundArguments = 0;
    for (int i = 0; i < numberOfArguments; ++i) {
      Object argument = boundArguments.get(i);
      if (argument != null)
        ++numberOfBoundArguments;
      else
        argument = t[i - numberOfBoundArguments];

      arguments[i] = argument;
    }
    return invokeFactory.apply(arguments);
  }

}
