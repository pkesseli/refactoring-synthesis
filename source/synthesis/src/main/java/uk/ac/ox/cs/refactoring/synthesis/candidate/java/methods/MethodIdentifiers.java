package uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;

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
    for (int i = 0; i < method.getNumberOfParams(); ++i) {
      final String javaParserName = method.getParam(i).getType().describe();
      final String nameWithoutGenerics = javaParserName.replaceAll("<[^>]*>", "");
      parameterTypes.add(nameWithoutGenerics);
    }

    final String fullyQualifiedClassName = getFullyQualifiedName(method.declaringType());
    return new MethodIdentifier(fullyQualifiedClassName, method.getName(), parameterTypes);
  }

  /**
   * Equivalent to {@link ResolvedReferenceTypeDeclaration#getQualifiedName()},
   * but separates inner classes using
   * {@link JavaLanguage#INNER_CLASS_SEPARATOR} rather than
   * {@link JavaLanguage#PACKAGE_SEPARATOR}.
   * 
   * @param type Type for which to generate a name.
   * @return Fully qualified name compatible with Java class loaders.
   */
  public static String getFullyQualifiedName(final ResolvedReferenceTypeDeclaration type) {
    final String qualifiedName = type.getQualifiedName();
    final String packageName = type.getPackageName();
    final String packagePrefix = packageName != null ? packageName : "";
    final int classNameOffset = packagePrefix.isEmpty() ? 0 : packagePrefix.length() + 1;
    final String className = qualifiedName.substring(classNameOffset);
    final String escapedClassName = className.replace(JavaLanguage.PACKAGE_SEPARATOR,
        JavaLanguage.INNER_CLASS_SEPARATOR);

    if (packagePrefix.isEmpty())
      return escapedClassName;
    return packagePrefix + JavaLanguage.PACKAGE_SEPARATOR + escapedClassName;
  }

  private MethodIdentifiers() {
  }
}
