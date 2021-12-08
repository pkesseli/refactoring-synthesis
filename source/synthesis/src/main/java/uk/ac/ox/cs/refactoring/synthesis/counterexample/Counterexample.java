package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models a CEGIS counterexample in the form of a state of a Java program based
 * on which a Java method can be invoked.
 */
public class Counterexample {

  /**
   * Instance on which to invoke the method. <code>null</code> in the case of
   * static methods.
   */
  public final Object Instance;

  /** Arguments using which the method was invoked. */
  public final List<Object> Arguments = new ArrayList<>();

  /** Values of all static fields involved. */
  public final Map<String, Object> Fields = new HashMap<>();

  /** @param instance {@link #Instance} */
  public Counterexample(final Object instance) {
    Instance = instance;
  }
}
