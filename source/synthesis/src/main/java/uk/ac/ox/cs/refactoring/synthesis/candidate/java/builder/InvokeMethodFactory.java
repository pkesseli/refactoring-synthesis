package uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.InvokeMethod;

/**
 * Adapter from {@link Method#invoke(Object, Object...)} to {@link Function}.
 */
public class InvokeMethodFactory extends InvokeFactory<InvokeMethod> {

  /**
   * Wrapped {@link Method}.
   */
  private final Method method;

  /**
   * @param method {@link #method}
   */
  public InvokeMethodFactory(final Method method) {
    super(!Modifier.isStatic(method.getModifiers()));
    this.method = method;
  }

  @Override
  public InvokeMethod apply(final Object[] t) {
    return new InvokeMethod(getInstance(t), getArguments(t), method);
  }

}