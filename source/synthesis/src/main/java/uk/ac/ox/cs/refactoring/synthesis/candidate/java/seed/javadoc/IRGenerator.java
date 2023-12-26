package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.Map;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

public class IRGenerator {
  private final IRGenerationVisitor visitor;

  public IRGenerator(final ClassLoader classLoader, final JavaParser javaParser, final TypeSolver typeSolver,
      final Set<String> involvedClasses, final Map<String, IExpression> environment,
      final SnippetCandidate candidate) {
    this.visitor = new IRGenerationVisitor(classLoader, javaParser, typeSolver, involvedClasses, environment,
        candidate);
  }

  public IExpression convertExpression(Expression expr) {
    expr.accept(visitor, null);
    return visitor.getExpression();
  }
}
