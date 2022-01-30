package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import com.fasterxml.classmate.members.ResolvedMethod;

/** Finds a {@link ResolvedMethod} for a given {@link Method}. */
class MethodFilter implements Predicate<ResolvedMethod> {

  /** Method to identify. */
  private final Method method;

  /** @param method {@link #method} */
  MethodFilter(final Method method) {
    this.method = method;
  }

  @Override
  public boolean test(final ResolvedMethod t) {
    if (!method.getName().equals(t.getName()))
      return false;

    final Class<?>[] parameterTypes = method.getParameterTypes();
    if (parameterTypes.length != t.getArgumentCount())
      return false;
    for (int i = 0; i < parameterTypes.length; ++i)
      if (parameterTypes[i] != t.getArgumentType(i).getErasedType())
        return false;
    return true;
  }
}
