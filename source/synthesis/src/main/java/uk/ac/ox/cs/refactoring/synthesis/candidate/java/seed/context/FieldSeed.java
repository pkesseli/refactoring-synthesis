package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.util.List;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;

/**
 * Adds a configurable list of field accesses to the instruction set.
 */
public class FieldSeed implements InstructionSetSeed {

  /**
   * Fields to add to the instruction set.
   */
  private final List<FieldAccess> fields;

  /**
   * @param fields {@link #fields}
   */
  public FieldSeed(final List<FieldAccess> fields) {
    this.fields = fields;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final JavaComponents javaComponents = new JavaComponents(components);
    for (final FieldAccess field : fields)
      javaComponents.lhs(field.getType(), new NullaryComponent<>(field));
  }

}
