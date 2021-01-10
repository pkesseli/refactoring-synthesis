package uk.ac.ox.cs.refactoring.classloader;

import java.security.SecureClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Extends the default Java {@link ClassLoader} API by making the set of classes
 * loaded by <code>this</code> class loader available. This is intended for
 * algorithms which need to compare heap states and thus need to be know which
 * classes were loaded, since their static state might have been mutated.
 */
public abstract class IsolatedClassLoader extends SecureClassLoader {
  /**
   * @param classLoader {@link SecureClassLoader#SecureClassLoader(ClassLoader)}
   */
  public IsolatedClassLoader(final ClassLoader classLoader) {
    super(classLoader);
  }

  /**
   * Mutable view of {@link #LoadedClasses}.
   */
  protected final Set<String> loadedClasses = new HashSet<>();

  /**
   * Contains all classes loaded directly by <code>this</code>
   * {@link ClassLoader}.
   */
  public final Set<String> LoadedClasses = Collections.unmodifiableSet(loadedClasses);
}
