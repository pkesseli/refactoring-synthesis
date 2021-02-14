package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

/**
 * Models a Java language expression in candidates.
 */
public interface IExpression extends INodeConvertible<Expression> {
  /**
   * Expression's type.
   */
  Type getType();

  /**
   * Evaluates the modeled expression, usually in isolation by virtue of loading
   * the class in an isolated class loader.
   */
  Object evaluate();
}
