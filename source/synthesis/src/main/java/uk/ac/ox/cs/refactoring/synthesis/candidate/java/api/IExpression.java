package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.InvocationTargetException;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;

/**
 * Models a Java language expression in candidates.
 */
public interface IExpression extends INodeConvertible<Expression> {
  /**
   * Expression's type.
   */
  Type getType();

  /**
   * Evaluates the modeled expression in the given {@link ExecutionContext}.
   * 
   * @param context {@link ExecutionContext} in which to evaluate the expression.
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws InvocationTargetException
   * @throws NoSuchFieldException
   */
  Object evaluate(ExecutionContext context) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException;
}
