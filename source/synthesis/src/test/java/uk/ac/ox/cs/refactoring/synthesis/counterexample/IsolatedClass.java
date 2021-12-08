package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import org.opentest4j.AssertionFailedError;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;

public class IsolatedClass {
  
  public final Class<?> Class;

  public IsolatedClass(final ClassLoader classLoader, final String name) {
    try {
      Class = ClassLoaders.loadClass(classLoader, name);
    } catch (final ClassNotFoundException e) {
      throw new AssertionFailedError(null, e);
    }
  }
}
