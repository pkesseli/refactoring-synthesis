package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.lang.reflect.Method;
import java.util.Set;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Cast;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;

/**
 * Heper to add {@link Cast} to components if the expected return type is not in
 * the current components. If the expected return type is a primitive number,
 * and we have other pimitive number types in the existing components, we add
 * appropriate casts.
 */
public class NumericCastSeed implements InstructionSetSeed {

  /** Used to load the method to refactor in order to determin its return type. */
  private final ClassLoader classLoader;

  /** Method to be refactored. */
  private final MethodIdentifier methodToRefactor;

  /**
   * @param classLoader      {@link #classLoader}
   * @param methodToRefactor {@link #methodToRefactor}
   */
  public NumericCastSeed(final ClassLoader classLoader, final MethodIdentifier methodToRefactor) {
    this.classLoader = classLoader;
    this.methodToRefactor = methodToRefactor;
  }

  @Override
  public void seed(final ComponentDirectory components) throws NoSuchMethodException, ClassNotFoundException {
    final Method method = Methods.getMethod(classLoader, methodToRefactor);
    final Class<?> returnType = method.getReturnType();
    final Set<JavaLanguageKey> availableTypes = components.keySet(JavaLanguageKey.class);
    final boolean hasInt = has(availableTypes, Primitive.INT);
    final boolean hasByte = has(availableTypes, Primitive.BYTE);
    if (returnType == byte.class && !hasByte && hasInt || returnType == int.class && !hasInt && hasByte)
      Cast.register(new JavaComponents(components));
  }

  /**
   * Indicates whether there are any components of type {@code primitive}.
   * 
   * @param availableTypes All available types in the component set.
   * @param primitive      Expected primitive type
   * @return {@code true} if the primitive type is already present, {@code false}
   *         otherwise.
   */
  private static boolean has(final Set<JavaLanguageKey> availableTypes, final Primitive primitive) {
    return availableTypes.stream().map(availableType -> availableType.Type)
        .filter(type -> type.isPrimitiveType()).map(Type::asPrimitiveType)
        .anyMatch(primitiveType -> primitiveType.getType() == primitive);
  }
}
