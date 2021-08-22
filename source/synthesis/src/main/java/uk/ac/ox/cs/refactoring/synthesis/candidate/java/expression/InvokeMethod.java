package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.TypeExpr;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.MethodInvokable;

/**
 * Models a Java method invocation expression.
 */
public class InvokeMethod extends InvokeInvokable {

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