package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

/**
 * Handles 'simple' method calls only
 */
public class ResolveArgumentVisitor extends VoidVisitorAdapter<Void> {
  public final Map<String, IExpression> arguments;

  public final MethodIdentifier methodToRefactor;

  private final JavaParser javaParser;

  public ResolveArgumentVisitor(final Map<String, IExpression> arguments, final MethodIdentifier methodToRefactor,
      final JavaParser javaParser) {
    this.arguments = arguments;
    this.methodToRefactor = methodToRefactor;
    this.javaParser = javaParser;
  }

  @Override
  public void visit(final MethodCallExpr n, final Void unused) {
    super.visit(n, unused);
    if (!n.getName().asString().equals(methodToRefactor.MethodName)) {
      return;
    }

    final List<Expression> arguments = n.getArguments();
    for (int index = 0; index < arguments.size(); index++) {
      if (arguments.get(index) instanceof NameExpr) {
        final NameExpr argument = (NameExpr) arguments.get(index);
        final IExpression argumentExpr = new Parameter(index,
            javaParser.parseType(methodToRefactor.FullyQualifiedParameterTypeNames.get(index)).getResult().get());
        this.arguments.put(argument.getNameAsString(), argumentExpr);
      }
    }

    if (n.getScope().isPresent()) {
      final Expression scope = n.getScope().get();
      if (scope instanceof NameExpr) {
        final NameExpr receiver = (NameExpr) scope;
        final IExpression receiverExpr = This
            .create(javaParser.parseType(methodToRefactor.FullyQualifiedClassName).getResult().get());
        this.arguments.put(receiver.getNameAsString(), receiverExpr);
      }
    }

  }
}
