package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;

/**
 * Adds well-known constant expressions to the instruction set.
 */
public class ConstantSeed implements InstructionSetSeed {

  /** Types for which we have already inserted constants. */
  private final Set<Type> seededConstantTypes = new HashSet<>();

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final Set<JavaLanguageKey> keys = components.parameterKeySet(JavaLanguageKey.class);
    final JavaComponents javaComponents = new JavaComponents(components);
    for (final JavaLanguageKey key : keys) {
      final Type type = key.Type;
      if (seededConstantTypes.add(type))
        seed(javaComponents, key);
    }
  }

  /**
   * Adds constants for the given parameter key.
   * 
   * @param javaComponents Instruction set to extend with constants.
   * @param key            Type for which to generate constants.
   * @return {@code true} if instructions were added, {@code false} otherwise.
   */
  public boolean seed(final JavaComponents javaComponents, final JavaLanguageKey key) {
    final Type keyType = key.Type;
    if (keyType.isPrimitiveType()) {
      switch (keyType.asPrimitiveType().getType()) {
        case BOOLEAN:
          javaComponents.literal(keyType, false);
          javaComponents.literal(keyType, true);
          break;
        case BYTE:
          javaComponents.literal(keyType, (byte) 0);
          break;
        case CHAR:
          javaComponents.literal(keyType, '\0');
          break;
        case DOUBLE:
          javaComponents.literal(keyType, 0.0);
          break;
        case FLOAT:
          javaComponents.literal(keyType, 0.0f);
          break;
        case INT:
          javaComponents.literal(keyType, 0);
          javaComponents.literal(keyType, 11);
          break;
        case LONG:
          javaComponents.literal(keyType, 0L);
          break;
        case SHORT:
          javaComponents.literal(keyType, (short) 0);
          break;
        default:
          return false;
      }
      return true;
    }
    if (!key.Nonnull) {
      javaComponents.nullLiteral(keyType);
      return true;
    }
    return false;
  }
}
