package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Models a CEGIS counterexample in the form of a state of a Java program based
 * on which a Java method can be invoked.
 */
public class Counterexample {
  /**
   * Instance on which to invoke the method. <code>null</code> in the case of
   * static methods.
   */
  public final ObjectDescription Instance;

  /**
   * Literal method argumetns.
   */
  public final SortedMap<Integer, Object> LiteralArguments = new TreeMap<>();

  /**
   * Literal static field values.
   */
  public final Map<String, Object> LiteralFields = new HashMap<>();

  /**
   * Method arguments of object types.
   */
  public final SortedMap<Integer, ObjectDescription> ObjectArguments = new TreeMap<>();

  /**
   * Static field values of object types.
   */
  public final Map<String, ObjectDescription> ObjectFields = new HashMap<>();

  /**
   * @param instance {@link #Instance}
   */
  public Counterexample(final ObjectDescription instance) {
    Instance = instance;
  }
}
