package uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression;

import java.lang.reflect.Constructor;
import java.util.List;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ConstructorInvokable;

/**
 * Models a Java constructor invocation expression.
 */
public class InvokeConstructor extends InvokeInvokable {

  /**
   * @param arguments   {@link InvokeInvokable#InvokeInvokable(IExpression, List, Invokable)}
   * @param constructor {@link InvokeInvokable#InvokeInvokable(IExpression, List, Invokable)}
   */
  public InvokeConstructor(final List<IExpression> arguments, final Constructor<?> constructor) {
    super(null, arguments, new ConstructorInvokable(constructor));
  }

  @Override
  public Expression toNode() {
    final ClassOrInterfaceType type = TypeFactory.createClassType(invokable.getReturnType());
    return new ObjectCreationExpr(null, type, getArguments());
  }

}