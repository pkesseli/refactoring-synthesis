package uk.ac.ox.cs.refactoring.synthesis.candidate.java.type;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

/**
 * 
 */
public final class TypeFactory {

  /**
   * 
   */
  private static final String SCOPE_SEPARATOR = "\\.";

  private TypeFactory() {
  }

  /**
   * 
   * @param cls
   * @return
   */
  public static Type create(final Class<?> cls) {
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
   * 
   * @param cls
   * @return
   */
  private static ClassOrInterfaceType createClassType(final Class<?> cls) {
    return null;
  }

  /**
   * 
   * @param pkg
   * @return
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
