package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;

public class SourceCodeConvertor {
  private final AnotherSnippetComponentVisitor visitor;

  public SourceCodeConvertor(final ClassLoader classLoader, final JavaParser javaParser, final TypeSolver typeSolver,
      final Set<String> involvedClasses) {
    this.visitor = new AnotherSnippetComponentVisitor(classLoader, javaParser, typeSolver, involvedClasses);
  }

  public IExpression convertExpression(Expression expr) {
    expr.accept(visitor, null);
    return visitor.getExpression();
  }
}
