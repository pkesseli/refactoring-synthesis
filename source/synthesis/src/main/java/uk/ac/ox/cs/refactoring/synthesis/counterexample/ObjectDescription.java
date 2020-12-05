package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.HashMap;
import java.util.Map;

public class ObjectDescription {
  /**
   * 
   */
  public final String FullyQualifiedClassName;

  /**
   * 
   */
  public final Map<String, Object> LiteralFields = new HashMap<>();

  /**
   * 
   */
  public final Map<String, ObjectDescription> ObjectFields = new HashMap<>();

  /**
   * 
   */
  public ObjectDescription(final String fullyQualifiedClassName) {
    FullyQualifiedClassName = fullyQualifiedClassName;
  }
}
