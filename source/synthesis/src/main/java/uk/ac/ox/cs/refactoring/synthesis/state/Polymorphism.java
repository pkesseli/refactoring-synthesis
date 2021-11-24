package uk.ac.ox.cs.refactoring.synthesis.state;

import java.lang.reflect.Modifier;

/**
 * Helper operations for handling types which cannot be instantiated directly.
 */
public final class Polymorphism {

  /** Package into which Mockito puts generated classes. */
  private static final String MOCKITO_CODE_GEN = "org.mockito.codegen";

  /**
   * Decides whether the given type can be instantiated directly.
   * 
   * @param type {@link Class} to check.
   * @return {@code true} if {@code type} is not an interface or abstract class.
   */
  public static boolean canBeInstantiated(final Class<?> type) {
    return !type.isInterface() && !Modifier.isAbstract(type.getModifiers());
  }

  /**
   * Since some {@link Class}es cannot be instantiated, the type of a created
   * object may be a subtype of its original object description. This helper
   * assumes we are pursuing a Mockito strategy in these cases and provides the
   * actual type to use when e.g. assigning fields (presumably this generated mock
   * type's parent).
   * 
   * @param created Generated object.
   * @return Type to use for field assignments.
   */
  public static Class<?> getFactoryType(final Object created) {
    final Class<?> cls = created.getClass();
    final Package pkg = cls.getPackage();
    if (pkg == null || !MOCKITO_CODE_GEN.equals(pkg.getName()))
      return cls;
    return cls.getSuperclass();
  }

  private Polymorphism() {
  }
}
