package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.function.Predicate;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

class MatchesSignature implements Predicate<MethodDeclaration> {

  private final TypeSolver typeSolver;

  private final MethodIdentifier methodIdentifier;

  /**
   * @param typeSolver       {@link #typeSolver}
   * @param methodIdentifier {@link #methodIdentifier}
   */
  MatchesSignature(final TypeSolver typeSolver, final MethodIdentifier methodIdentifier) {
    this.typeSolver = typeSolver;
    this.methodIdentifier = methodIdentifier;
  }

  @Override
  public boolean test(final MethodDeclaration method) {
    final String simpleName = method.getName().getIdentifier();
    if (!methodIdentifier.MethodName.equals(simpleName))
      return false;

    final int numberOfParameters = methodIdentifier.FullyQualifiedParameterTypeNames.size();
    if (numberOfParameters != method.getParameters().size()) {
      return false;
    }
    for (int i = 0; i < numberOfParameters; ++i) {
      final ResolvedType nodeType = method.getParameter(i).getType().resolve();
      final ResolvedReferenceTypeDeclaration identifierTypeDeclaration = typeSolver
          .solveType(methodIdentifier.FullyQualifiedParameterTypeNames.get(i));
      final ResolvedType identifierType = ReferenceTypeImpl.undeterminedParameters(identifierTypeDeclaration,
          typeSolver);
      if (!nodeType.equals(identifierType)) {
        return false;
      }
    }
    return true;
  }

}
