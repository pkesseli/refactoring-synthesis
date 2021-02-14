package uk.ac.ox.cs.refactoring.synthesis.candidate.java.type;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

/**
 * Helper to construct {@link Type}s.
 */
public final class TypeFactory {

  /**
   * Regular expression for Java scope separators.
   */
  private static final String SCOPE_SEPARATOR = "\\.";

  private TypeFactory() {
  }

  /**
   * Converts a Java {@link Class} to an equivalent {@link Type}.
   * 
   * @param cls Java {@link Class} to convert.
   * @return AST {@link Type}.
   */
  public static Type create(final Class<?> cls) {
    if (cls.isPrimitive()) {
      return PrimitiveType.doubleType();
    }

    return createClassType(cls);
  }

  /**
   * Converts a non-primitive {@link Class} to a {@link Type}.
   * 
   * @param cls Java {@link Class} to convert.
   * @return AST {@link Type}.
   */
  private static ClassOrInterfaceType createClassType(final Class<?> cls) {
    final Class<?> declaringClass = cls.getDeclaringClass();
    final ClassOrInterfaceType scope;
    if (declaringClass != null) {
      scope = createClassType(declaringClass);
    } else {
      scope = createPackage(cls.getPackage());
    }
    return new ClassOrInterfaceType(scope, cls.getSimpleName());
  }

  /**
   * Creates a package scope.
   * 
   * @param pkg {@link Package} to convert.
   * @return AST package scope.
   */
  private static ClassOrInterfaceType createPackage(final Package pkg) {
    if (pkg == null) {
      return null;
    }
    final String name = pkg.getName();
    if (name.isEmpty()) {
      return null;
    }
    final String[] components = name.split(SCOPE_SEPARATOR);
    ClassOrInterfaceType result = new ClassOrInterfaceType(null, components[0]);
    for (int i = 1; i < components.length; ++i) {
      result = new ClassOrInterfaceType(result, components[i]);
    }
    return result;
  }
}
