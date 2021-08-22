package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Function;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeConstructor;

/**
 * Adapter from {@link Method#invoke(Object, Object...)} to {@link Function}.
 */
public class InvokeConstructorFactory extends InvokeFactory<InvokeConstructor> {

  /**
   * Wrapped {@link Constructor}.
   */
  private final Constructor<?> constructor;

  /**
   * @param constructor {@link #constructor}
   */
  public InvokeConstructorFactory(final Constructor<?> constructor) {
    super(false);
    this.constructor = constructor;
  }

  @Override
  public InvokeConstructor apply(final Object[] t) {
    return new InvokeConstructor(getArguments(t), constructor);
  }

}