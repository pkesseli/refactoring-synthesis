package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeInvokable;

/**
 * Base for a adapters from invokables to {@link Function}.
 */
abstract class InvokeFactory<T extends InvokeInvokable> implements Function<Object[], T> {

  private final boolean hasInstance;

  InvokeFactory(final boolean hasInstance) {
    this.hasInstance = hasInstance;
  }

  protected IExpression getInstance(final Object[] t) {
    if (!hasInstance)
      return null;
    return (IExpression) t[0];
  }

  protected List<IExpression> getArguments(final Object[] t) {
    Stream<IExpression> arguments = Arrays.stream(t).map(IExpression.class::cast);
    if (hasInstance)
      arguments = arguments.skip(1);
    return arguments.collect(Collectors.toList());
  }

}