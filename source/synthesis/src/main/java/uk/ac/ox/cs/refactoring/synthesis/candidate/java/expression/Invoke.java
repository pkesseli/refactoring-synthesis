package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Constructor;
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
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.FunctionComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
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

  public static abstract class InvokeInvokable implements IExpression {

    /**
     * {@code this} expression for the method invocation. {@code null} for static
     * methods.
     */
    protected final IExpression instance;

    /**
     * Method argument values.
     */
    private final List<IExpression> arguments;

    protected final Invokable invokable;

    public InvokeInvokable(final IExpression instance, final List<IExpression> arguments, final Invokable invokable) {
      this.instance = instance;
      this.arguments = arguments;
      this.invokable = invokable;
    }

    @Override
    public Type getType() {
      return TypeFactory.create(invokable.getReturnType());
    }

    @Override
    public Object evaluate(ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
        InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
      final Object obj;
      if (Invokable.hasInstance(invokable)) {
        obj = null;
      } else {
        obj = instance.evaluate(context);
      }

      final Object[] args = new Object[arguments.size()];
      for (int i = 0; i < args.length; ++i) {
        args[i] = arguments.get(i).evaluate(context);
      }

      return invokable.invoke(obj, args);
    }

    public NodeList<Expression> getArguments() {
      return arguments.stream().map(IExpression::toNode)
          .collect(Collectors.<Expression, NodeList<Expression>>toCollection(NodeList::new));
    }

  }

  /**
   * Models a Java method invocation expression.
   */
  public static class InvokeMethod extends InvokeInvokable {

    /**
     * Reflective method used for evaluation.
     */
    private final Method method;

    /**
     * @param instance  {@link InvokeInvokable#InvokeInvokable(IExpression, List, Invokable)}
     * @param arguments {@link InvokeInvokable#InvokeInvokable(IExpression, List, Invokable)}
     * @param method    {@link #method}
     */
    public InvokeMethod(final IExpression instance, final List<IExpression> arguments, final Method method) {
      super(instance, arguments, new MethodInvokable(method));
      this.method = method;
    }

    @Override
    public Expression toNode() {
      final String name = method.getName();
      final Expression scope;
      if (Modifier.isStatic(method.getModifiers())) {
        scope = new TypeExpr(TypeFactory.createClassType(method.getDeclaringClass()));
      } else {
        scope = instance.toNode();
      }
      return new MethodCallExpr(scope, name, getArguments());
    }

  }

  private static class InvokeConstructor extends InvokeInvokable {

    public InvokeConstructor(final IExpression instance, final List<IExpression> arguments,
        final Constructor<?> constructor) {
      super(instance, arguments, new ConstructorInvokable(constructor));
    }

    @Override
    public Expression toNode() {
      final ClassOrInterfaceType type = TypeFactory.createClassType(invokable.getReturnType());
      return new ObjectCreationExpr(null, type, getArguments());
    }

  }

  /**
   * Adapter from {@link Method#invoke(Object, Object...)} to {@link Function}.
   */
  private static abstract class InvokeFactory {

    private final boolean hasInstance;

    public InvokeFactory(final boolean hasInstance) {
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

  /**
   * Adapter from {@link Method#invoke(Object, Object...)} to {@link Function}.
   */
  private static class InvokeMethodFactory extends InvokeFactory implements Function<Object[], InvokeMethod> {

    /**
     * Wrapped {@link Method}.
     */
    private final Method method;

    /**
     * @param method {@link #method}
     */
    InvokeMethodFactory(final Method method) {
      super(!Modifier.isStatic(method.getModifiers()));
      this.method = method;
    }

    @Override
    public InvokeMethod apply(final Object[] t) {
      return new InvokeMethod(getInstance(t), getArguments(t), method);
    }

  }

  /**
   * Adapter from {@link Method#invoke(Object, Object...)} to {@link Function}.
   */
  private static class InvokeConstructorFactory extends InvokeFactory implements Function<Object[], InvokeConstructor> {

    /**
     * Wrapped {@link Constructor}.
     */
    private final Constructor<?> constructor;

    /**
     * @param constructor {@link #constructor}
     */
    InvokeConstructorFactory(final Constructor<?> constructor) {
      super(false);
      this.constructor = constructor;
    }

    @Override
    public InvokeConstructor apply(final Object[] t) {
      return new InvokeConstructor(getInstance(t), getArguments(t), constructor);
    }

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
    components.nonnull(type, new FunctionComponent<>(parameterKeys, new InvokeMethodFactory(method)));
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
