package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 */
public class Counterexample {
  /**
   * 
   */
  public final ObjectDescription Instance;

  /**
   * 
   */
  public final SortedMap<Integer, Object> LiteralArguments = new TreeMap<>();

  /**
   * 
   */
  public final Map<String, Object> LiteralFields = new HashMap<>();

  /**
   * 
   */
  public final SortedMap<Integer, ObjectDescription> ObjectArguments = new TreeMap<>();

  /**
   * 
   */
  public final Map<String, ObjectDescription> ObjectFields = new HashMap<>();

  /**
   * @param instance
   */
  public Counterexample(final ObjectDescription instance) {
    Instance = instance;
  }
}
