package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Double;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Integer;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ConstructorInvokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.MethodInvokable;

/**
 * Adds instructions based on the types used in a method to be replaced.
 */
public class TypeSeed implements InstructionSetSeed {

  /**
   * Used to load classes reflectively for analysis.
   */
  private final ClassLoader classLoader;

  /**
   * Method to be replaced.
   */
  private final MethodIdentifier methodToRefactor;

  /**
   * @param classLoader      {@link #classLoader}
   * @param methodToRefactor {@link #methodToRefactor}
   */
  public TypeSeed(final ClassLoader classLoader, final MethodIdentifier methodToRefactor) {
    this.classLoader = classLoader;
    this.methodToRefactor = methodToRefactor;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final Invokable invokable = Methods.create(classLoader, methodToRefactor);
    final JavaComponents javaComponents = new JavaComponents(components);
    final Set<Class<?>> allTypes = new HashSet<>();
    allTypes.addAll(invokable.getParameterTypes());
    allTypes.add(invokable.getInstanceType());
    allTypes.add(invokable.getReturnType());
    for (final Class<?> type : allTypes)
      add(javaComponents, allTypes, type);
  }

  private static void add(final JavaComponents javaComponents, final Set<Class<?>> allTypes, final Class<?> type) {
    if (Void.class == type)
      return;

    if (int.class == type)
      Integer.register(javaComponents);
    else if (double.class == type)
      Double.register(javaComponents);
    else {
      for (final Method method : type.getDeclaredMethods())
        if (isRelevant(allTypes, method, new MethodInvokable(method)))
          Invoke.register(javaComponents, method);
      for (final Constructor<?> constructor : type.getDeclaredConstructors())
        if (isRelevant(allTypes, constructor, new ConstructorInvokable(constructor)))
          Invoke.register(javaComponents, constructor);
    }
  }

  private static boolean isRelevant(final Set<Class<?>> allTypes, final Executable executable,
      final Invokable invokable) {
    if (executable.getAnnotation(Deprecated.class) != null)
      return false;

    for (final Class<?> type : invokable.getParameterTypes())
      if (!allTypes.contains(type))
        return false;
    return allTypes.contains(invokable.getReturnType());
  }

}
