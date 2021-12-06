package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;

/** Factory for {@link ObjenesisWrapper}. */
public class ObjenesisFactory {

  /**
   * Loads the class {@link org.objenesis.Objenesis Objenesis} in the given
   * {@link ClassLoader} and provides an instance of it, without loading the class
   * in this class' {@link ClassLoader}. The intent of this method is to
   * instantiate the objenesis object factory in the class loader in which we
   * intend to use it to create instances.
   * 
   * We load {@link org.objenesis.Objenesis Objenesis} in the target class loader
   * since we anticipate that instantiating certain objects will trigger loading
   * previously unloaded classes, and those should be loaded in the isolated class
   * loader as well.
   * 
   * @param classLoader {@link ClassLoader} in which to load the
   *                    {@link org.objenesis.Objenesis Objenesis} class.
   * @return {@link org.objenesis.Objenesis Objenesis} with which to instantiate
   *         objects in the given {@link ClassLoader}.
   */
  public static Function<Class<?>, Object> createObjenesis(final ClassLoader classLoader) {
    final String packageName = ObjenesisFactory.class.getPackageName();
    final String name = packageName + JavaLanguage.PACKAGE_SEPARATOR + "ObjenesisWrapper";
    final Class<?> cls;
    try {
      cls = classLoader.loadClass(name);
    } catch (final ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
    final Constructor<?> constructor;
    try {
      constructor = cls.getDeclaredConstructor();
    } catch (final NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(e);
    }
    constructor.setAccessible(true);

    final Object instance;
    try {
      instance = constructor.newInstance();
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
    @SuppressWarnings("unchecked")
    final Function<Class<?>, Object> objenesis = (Function<Class<?>, Object>) instance;
    return objenesis;
  }
}
