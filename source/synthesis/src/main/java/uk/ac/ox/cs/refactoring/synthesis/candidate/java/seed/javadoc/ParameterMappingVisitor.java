package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.IExpression;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifiers;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;


/**
 * Handles 'simple' method calls only
 */
public class ParameterMappingVisitor extends VoidVisitorAdapter<Void> {
  public final Map<String, IExpression> arguments;

  public final MethodIdentifier methodToRefactor;

  private final JavaParser javaParser;

  public ParameterMappingVisitor(final Map<String, IExpression> arguments, final MethodIdentifier methodToRefactor, final JavaParser javaParser) {
    this.arguments = arguments;
    this.methodToRefactor = methodToRefactor;
    this.javaParser = javaParser;
  }

  @Override
  public void visit(final MethodCallExpr n, final Void unused) {
    super.visit(n, unused);
    // final ResolvedMethodDeclaration method = n.resolve();

    // final MethodIdentifier methodIdentifier = MethodIdentifiers.create(method);

    // if (!methodToRefactor.FullyQualifiedClassName.equals(methodIdentifier.FullyQualifiedClassName)) {
    //   return;
    // }
    // if (!methodToRefactor.MethodName.equals(methodIdentifier.MethodName)) {
    //   return;
    // }
    // // no zip function
    // if (methodToRefactor.FullyQualifiedParameterTypeNames.size() != methodIdentifier.FullyQualifiedParameterTypeNames.size()) {
    //   return;
    // }
    // for (int i = 0; i < methodToRefactor.FullyQualifiedParameterTypeNames.size(); i++) {
    //   if (!methodToRefactor.FullyQualifiedParameterTypeNames.get(i).equals(methodIdentifier.FullyQualifiedParameterTypeNames.get(i))) {
    //     return;
    //   }
    // }
    if (!n.getName().asString().equals(methodToRefactor.MethodName)) {
      return;
    }

    final List<Expression> arguments = n.getArguments();
    for (int index = 0; index < arguments.size(); index++) {
      if (arguments.get(index) instanceof NameExpr) {
        final NameExpr argument = (NameExpr) arguments.get(index);
        // final IExpression argumentExpr = new Parameter(index, getType(argument));
        final IExpression argumentExpr = new Parameter(index, javaParser.parseType(methodToRefactor.FullyQualifiedParameterTypeNames.get(index)).getResult().get());
        this.arguments.put(argument.getNameAsString(), argumentExpr);
      }
    }

    if (n.getScope().isPresent()) {
      final Expression scope = n.getScope().get();
      if (scope instanceof NameExpr) {
        final NameExpr receiver = (NameExpr) scope;
        // final IExpression receiverExpr = This.create(getType(receiver));
        final IExpression receiverExpr = This.create(javaParser.parseType(methodToRefactor.FullyQualifiedClassName).getResult().get());
        this.arguments.put(receiver.getNameAsString(), receiverExpr);
      }
    }

  }

  /**
   * Determinse the {@link Type} of the given {@link Expression}.
   * 
   * @param expression {@link Expression} whose type to discover.
   * @return {@link Type} of {@code expression}.
   */
  private Type getType(final Expression expression) {
    final ResolvedType resolvedType = expression.calculateResolvedType();
    return TypeFactory.create(javaParser, resolvedType);
  }
}
