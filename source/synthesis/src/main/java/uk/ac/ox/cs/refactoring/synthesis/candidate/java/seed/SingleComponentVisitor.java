package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;

class SingleComponentVisitor extends VoidVisitorAdapter<JavaComponents> {
  @Override
  public void visit(final MethodCallExpr n, final JavaComponents arg) {
    super.visit(n, arg);
  }
}
