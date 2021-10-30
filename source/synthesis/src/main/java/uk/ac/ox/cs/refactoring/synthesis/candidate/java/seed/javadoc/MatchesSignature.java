package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.function.Predicate;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;

/**
 * Checks whether a given {@link MethodDeclaration}'s simple name and parameter
 * types match a given {@link MethodIdentifier}.
 */
class MatchesSignature implements Predicate<MethodDeclaration> {

  /**
   * Used to resolve type names in {@link MethodIdentifier} to
   * {@link ResolvedType}s.
   */
  private final TypeSolver typeSolver;

  /**
   * Identifier which {@link MethodDeclaration}s should match.
   */
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
      final ResolvedType identifierType = resolve(methodIdentifier.FullyQualifiedParameterTypeNames.get(i));
      if (!nodeType.equals(identifierType)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Resolves a {@link ResolvedType} by its name.
   * 
   * @param fullyQualifiedParameterName Fully qualified type name.
   * @return Type identified by given name.
   */
  private ResolvedType resolve(final String fullyQualifiedParameterName) {
    try {
      return ResolvedPrimitiveType.byName(fullyQualifiedParameterName);
    } catch (final IllegalArgumentException ignored) {
    }
    final ResolvedReferenceTypeDeclaration identifierTypeDeclaration = typeSolver
        .solveType(fullyQualifiedParameterName);
    return ReferenceTypeImpl.undeterminedParameters(identifierTypeDeclaration, typeSolver);
  }

}
