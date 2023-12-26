package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.Map;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;

/**
 * Compiling simple expressions to our instruction set
 */
public class ExpressionCompiler {
  private final ExpressionCompilerVisitor visitor;

  public ExpressionCompiler(final ClassLoader classLoader, final JavaParser javaParser, final TypeSolver typeSolver,
      final Set<String> involvedClasses, final Map<String, IExpression> environment,
      final SnippetCandidate candidate) {
    this.visitor = new ExpressionCompilerVisitor(classLoader, javaParser, typeSolver, involvedClasses, environment,
        candidate);
  }

  public IExpression compile(Expression expr) {
    expr.accept(visitor, null);
    return visitor.getExpression();
  }
}
