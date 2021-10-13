package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.lang.reflect.InvocationTargetException;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

/** Placeholder allowing to replace an expression with parameters. */
class Placeholder implements IExpression {

  /** Replaced expression. */
  IExpression expression;

  @Override
  public Expression toNode() {
    return expression.toNode();
  }

  @Override
  public Type getType() {
    return expression.getType();
  }

  @Override
  public Object evaluate(final ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
    return expression.evaluate(context);
  }

}
