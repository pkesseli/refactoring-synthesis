package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;

/**
 * Base class for all expressions invoking a method or constructor.
 */
public abstract class InvokeInvokable implements IExpression {

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
      obj = instance.evaluate(context);
    } else {
      obj = null;
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