package uk.ac.ox.cs.refactoring.synthesis.factory;

import java.lang.reflect.InvocationTargetException;

public final class ObjectFactory {
  private ObjectFactory() {
  }

  public static <T> T create(final ClassLoader classLoader, final String className) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    final Class<?> cls = classLoader.loadClass(className);
    final Object instance = cls.getConstructor().newInstance();
    @SuppressWarnings("unchecked")
    final T result = (T) instance;
    return result;
  }
}
