package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

public class ParameterMapping {
  public final Map<String, IExpression> arguments;

  public final MethodIdentifier methodToRefactor;

  private final JavaParser javaParser;

  public ParameterMapping(final MethodIdentifier methodToRefactor, final JavaParser javaParser) {
    arguments = new HashMap<>();
    this.methodToRefactor = methodToRefactor;
    this.javaParser = javaParser;
  }

  public void checkExpression(final Expression expr) {
    final ParameterMappingVisitor visitor = new ParameterMappingVisitor(arguments, methodToRefactor, javaParser);
    expr.accept(visitor, null);
  }
}
