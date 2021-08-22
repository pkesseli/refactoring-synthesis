package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.FunctionComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.InvokeConstructorFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.InvokeMethodFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ConstructorInvokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.MethodInvokable;

/**
 * Contains Java method invocation expressions for use in builders.
 */
public final class Invoke {

  private Invoke() {
  }

  /**
   * Helper to register invoke method expressions in Java candidate builders.
   * 
   * @param components Directory in which to register all invoke method
   *                   expressions.
   * @param method
   */
  public static void register(final JavaComponents components, final Method method) {
    final Type type = TypeFactory.create(method.getReturnType());
    final List<JavaLanguageKey> parameterKeys = getParameterKeys(new MethodInvokable(method));
    components.nonnull(type, new FunctionComponent<JavaLanguageKey, InvokeMethod>(parameterKeys, new InvokeMethodFactory(method)));
  }

  /**
   * Helper to register invoke constructor expressions in Java candidate builders.
   * 
   * @param components Directory in which to register all invoke constructor
   *                   expressions.
   * @param method
   */
  public static void register(final JavaComponents components, final Constructor<?> constructor) {
    final Type type = TypeFactory.create(constructor.getDeclaringClass());
    final List<JavaLanguageKey> parameterKeys = getParameterKeys(new ConstructorInvokable(constructor));
    components.nonnull(type, new FunctionComponent<>(parameterKeys, new InvokeConstructorFactory(constructor)));
  }

  /**
   * Component keys required in the builder, based on the methods parameters.
   * 
   * @param invokable {@link Invokable} for which to define required builder
   *                  parameters.
   * @return {@link List} of component parameter keys.
   */
  private static List<JavaLanguageKey> getParameterKeys(final Invokable invokable) {
    final List<JavaLanguageKey> parameterKeys = new ArrayList<>();
    if (Invokable.hasInstance(invokable))
      parameterKeys.add(JavaLanguageKeys.nonnull(TypeFactory.create(invokable.getInstanceType())));
    for (final Class<?> parameterType : invokable.getParameterTypes())
      parameterKeys.add(JavaLanguageKeys.expression(TypeFactory.create(parameterType)));

    return parameterKeys;
  }

}
