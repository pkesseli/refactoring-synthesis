package uk.ac.ox.cs.refactoring.synthesis.candidate.java.type;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedVoidType;

import org.apache.commons.lang3.StringUtils;

/** Helper to construct {@link Type}s. */
public final class TypeFactory {

  /** Regular expression for Java scope separators. */
  private static final String SCOPE_SEPARATOR_PATTERN = "\\.";

  private TypeFactory() {
  }

  /**
   * Converts a Java {@link Class} to an equivalent {@link Type}.
   * 
   * @param cls Java {@link Class} to convert.
   * @return AST {@link Type}.
   */
  public static Type create(final Class<?> cls) {
    if (cls == null) {
      return null;
    }

    if (cls.isPrimitive()) {
      if (void.class == cls)
        return new VoidType();
      if (boolean.class == cls)
        return PrimitiveType.booleanType();
      if (byte.class == cls)
        return PrimitiveType.byteType();
      if (short.class == cls)
        return PrimitiveType.shortType();
      if (int.class == cls)
        return PrimitiveType.intType();
      if (long.class == cls)
        return PrimitiveType.longType();
      if (float.class == cls)
        return PrimitiveType.floatType();
      if (double.class == cls)
        return PrimitiveType.doubleType();
      if (char.class == cls)
        return PrimitiveType.charType();
      throw new IllegalArgumentException();
    }

    if (cls.isArray()) {
      final Type element = create(cls.getComponentType());
      return new ArrayType(element);
    }

    return createClassType(cls);
  }

  /**
   * Converts a non-primitive {@link Class} to a {@link Type}.
   * 
   * @param cls Java {@link Class} to convert.
   * @return AST {@link Type}.
   */
  public static ClassOrInterfaceType createClassType(final Class<?> cls) {
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
   * @return {@link #createPackage(String)}
   */
  private static ClassOrInterfaceType createPackage(final Package pkg) {
    if (pkg == null) {
      return null;
    }
    return createPackage(pkg.getName());
  }

  /**
   * Creates a package scope from a package name.
   * 
   * @param name Package name.
   * @return AST package scope.
   */
  private static ClassOrInterfaceType createPackage(final String name) {
    if (StringUtils.isEmpty(name)) {
      return null;
    }
    final String[] components = name.split(SCOPE_SEPARATOR_PATTERN);
    ClassOrInterfaceType result = new ClassOrInterfaceType(null, components[0]);
    for (int i = 1; i < components.length; ++i) {
      result = new ClassOrInterfaceType(result, components[i]);
    }
    return result;
  }

  /**
   * Create an unresolved {@link Type} from a {@link ResolvedType}. Resynth
   * instructions in the synthesis phase are identified using {@link Type}, not
   * {@link ResolvedType}, as it is assumed that types cannot be resolved in all
   * synthesis environments.
   * 
   * @param javaParser   Parser which created the node resolving to
   *                     {@code resolvedType}.
   * @param resolvedType {@link ResolvedType} to convert.
   * @return {@link Type} equivalent to {@code resolvedType}.
   */
  public static Type create(final JavaParser javaParser, final ResolvedType resolvedType) {
    if (resolvedType instanceof ResolvedPrimitiveType) {
      final ResolvedPrimitiveType primitiveType = (ResolvedPrimitiveType) resolvedType;
      switch (primitiveType) {
        case BOOLEAN:
          return PrimitiveType.booleanType();
        case BYTE:
          return PrimitiveType.byteType();
        case CHAR:
          return PrimitiveType.charType();
        case DOUBLE:
          return PrimitiveType.doubleType();
        case FLOAT:
          return PrimitiveType.floatType();
        case INT:
          return PrimitiveType.intType();
        case LONG:
          return PrimitiveType.longType();
        case SHORT:
          return PrimitiveType.shortType();
      }
    } else if (resolvedType instanceof ResolvedReferenceType) {
      final ResolvedReferenceType resolvedReferenceType = (ResolvedReferenceType) resolvedType;
      final String name = resolvedReferenceType.getQualifiedName();
      return javaParser.parseClassOrInterfaceType(name).getResult().get();
    } else if (resolvedType instanceof ResolvedVoidType) {
      return new VoidType();
    } else if (resolvedType.isTypeVariable()) {
      // TODO: Support restricted type variables.
      return javaParser.parseClassOrInterfaceType("java.lang.Object").getResult().get();
    }
    throw new UnsupportedOperationException();
  }
}
