package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.opentest4j.AssertionFailedError;

public class IsolatedObject {

  public final IsolatedClass IsolatedClass;

  public final Object Object;

  public IsolatedObject(final ClassLoader classLoader, final String className) {
    IsolatedClass = new IsolatedClass(classLoader, className);
    try {
      Object = IsolatedClass.Class.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException
        | NoSuchMethodException e) {
      throw new AssertionFailedError(null, e);
    }
  }

  public void setField(String name, Object value) {
    try {
      FieldUtils.writeField(Object, name, value, true);
    } catch (IllegalAccessException e) {
      throw new AssertionFailedError(null, e);
    }
  }
}
