package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes an object and its field values as properties. Only the object's
 * class name as well as its field values are used to describe the object,
 * effectively reducing an object to its literal (nested) field values and its
 * reference identitys.
 */
public class ObjectDescription {
  /**
   * Fully qualified class name of the described object.
   */
  public final String FullyQualifiedClassName;

  /**
   * Values for literal fields in the described objet. Literal objects are Java
   * primitives (boxed and unboxed) as well as strings.
   */
  public final Map<String, Object> LiteralFields = new HashMap<>();

  /**
   * Values for complex object fields in the described projects.
   */
  public final Map<String, ObjectDescription> ObjectFields = new HashMap<>();

  /**
   * {@lilnk #FullyQualifiedClassName}
   */
  public ObjectDescription(final String fullyQualifiedClassName) {
    FullyQualifiedClassName = fullyQualifiedClassName;
  }
}
