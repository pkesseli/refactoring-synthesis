package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.FunctionComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Contains Java method invocation expressions for use in builders.
 */
public final class Invoke {

  private Invoke() {
  }

  /**
   * Models a Java method invocation expression.
   */
  public static class InvokeMethod implements IExpression {

    /**
     * {@code this} expression for the method invocation. {@code null} for static
     * methods.
     */
    private final IExpression instance;

    /**
     * Method argument values.
     */
    private final List<IExpression> arguments;

    /**
     * Reflective method used for evaluation.
     */
    private final Method method;

    /**
     * @param instance  {@link #instance}
     * @param arguments {@link #arguments}
     * @param method    {@link #method}
     */
    public InvokeMethod(final IExpression instance, final List<IExpression> arguments, final Method method) {
      this.instance = instance;
      this.arguments = arguments;
      this.method = method;
    }

    @Override
    public Expression toNode() {
      final NodeList<Expression> args = arguments.stream().map(IExpression::toNode)
          .collect(Collectors.<Expression, NodeList<Expression>>toCollection(NodeList::new));
      final String name = method.getName();
      final Expression scope;
      if (Modifier.isStatic(method.getModifiers())) {
        scope = new TypeExpr(TypeFactory.createClassType(method.getDeclaringClass()));
      } else {
        scope = instance.toNode();
      }
      return new MethodCallExpr(scope, name, args);
    }

    @Override
    public Type getType() {
      return TypeFactory.create(method.getReturnType());
    }

    @Override
    public Object evaluate(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      final Object obj;
      if (Modifier.isStatic(method.getModifiers())) {
        obj = null;
      } else {
        obj = instance.evaluate(context);
      }

      final Object[] args = new Object[arguments.size()];
      for (int i = 0; i < args.length; ++i) {
        args[i] = arguments.get(i).evaluate(context);
      }

      final Class<?> cls = context.ClassLoader.loadClass(method.getDeclaringClass().getName());
      final Class<?>[] parameterTypes = new Class<?>[method.getParameterTypes().length];
      for (int i = 0; i < parameterTypes.length; ++i)
        parameterTypes[i] = ClassLoaders.loadClass(context.ClassLoader, method.getParameterTypes()[i].getName());

      final Method isolatedMethod = cls.getDeclaredMethod(method.getName(), parameterTypes);
      return isolatedMethod.invoke(obj, args);
    }

  }

  /**
   * Adapter from {@link Method#invoke(Object, Object...)} to {@link Function}.
   */
  private static class InvokeFactory implements Function<Object[], InvokeMethod> {

    /**
     * Wrapped {@link Method}.
     */
    private final Method method;

    /**
     * @param method {@link #method}
     */
    InvokeFactory(final Method method) {
      this.method = method;
    }

    @Override
    public InvokeMethod apply(final Object[] t) {
      final IExpression instance;
      Stream<IExpression> arguments = Arrays.stream(t).map(IExpression.class::cast);
      if (Modifier.isStatic(method.getModifiers())) {
        instance = null;
      } else {
        instance = (IExpression) t[0];
        arguments = arguments.skip(1);
      }

      return new InvokeMethod(instance, arguments.collect(Collectors.toList()), method);
    }

  }

  /**
   * Helper to register invoke method expressions in Java candidate builders.
   * 
   * @param components Directory in which to register all invoke method
   *                   expressions.
   * @param methods
   */
  public static void register(final JavaComponents components, final Iterable<Method> methods) {
    for (final Method method : methods) {
      final Type type = TypeFactory.create(method.getReturnType());
      final List<JavaLanguageKey> parameterKeys = getParameterKeys(method);
      components.nonnull(type, new FunctionComponent<>(parameterKeys, new InvokeFactory(method)));
    }
  }

  /**
   * Component keys required in the builder, based on the methods parameters.
   * 
   * @param method {@link Method} for which to define required builder parameters.
   * @return {@link List} of component parameter keys.
   */
  private static List<JavaLanguageKey> getParameterKeys(final Method method) {
    final List<JavaLanguageKey> parameterKeys = new ArrayList<>();
    if (!Modifier.isStatic(method.getModifiers()))
      parameterKeys.add(JavaLanguageKeys.nonnull(TypeFactory.create(method.getDeclaringClass())));

    for (final Class<?> parameterType : method.getParameterTypes())
      parameterKeys.add(JavaLanguageKeys.expression(TypeFactory.create(parameterType)));

    return parameterKeys;
  }

}
