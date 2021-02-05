package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

public interface IExpression extends INodeConvertible<Expression> {
  Type getType();

  Object evaluate();
}
