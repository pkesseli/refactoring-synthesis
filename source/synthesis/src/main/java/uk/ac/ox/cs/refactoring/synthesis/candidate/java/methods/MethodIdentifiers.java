package uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

/** Factory for {@link MethodIdentifier}. */
public final class MethodIdentifiers {

  /**
   * Creates a {@link MethodIdentifier} for the given
   * {@link ResolvedMethodDeclaration}.
   * 
   * @param method {@link ResolvedMethodDeclaration} for which to create an
   *               identifier.
   * @return {@link MethodIdentifier} pointing to the method modelled in
   *         {@code method}.
   */
  public static MethodIdentifier create(final ResolvedMethodDeclaration method) {
    final List<String> parameterTypes = new ArrayList<>();
    for (int i = 0; i < method.getNumberOfParams(); ++i)
      parameterTypes.add(method.getParam(i).getType().describe());

    final String fullyQualifiedClassName = method.declaringType().getQualifiedName();
    return new MethodIdentifier(fullyQualifiedClassName, method.getName(), parameterTypes);
  }

  private MethodIdentifiers() {
  }
}
