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

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final Set<JavaLanguageKey> keys = components.parameterKeySet(JavaLanguageKey.class);
    final JavaComponents javaComponents = new JavaComponents(components);
    final Set<Type> processedTypes = new HashSet<>();
    for (final JavaLanguageKey key : keys) {
      final Type keyType = key.Type;
      if (processedTypes.contains(keyType))
        continue;
      if (key.Type.isPrimitiveType()) {
        switch (key.Type.asPrimitiveType().getType()) {
        case BOOLEAN:
          javaComponents.literal(keyType, false);
          javaComponents.literal(keyType, true);
          continue;
        case BYTE:
          javaComponents.literal(keyType, (byte) 0);
          continue;
        case CHAR:
          javaComponents.literal(keyType, '\0');
          continue;
        case DOUBLE:
          javaComponents.literal(keyType, 0.0);
          continue;
        case FLOAT:
          javaComponents.literal(keyType, 0.0f);
          continue;
        case INT:
          javaComponents.literal(keyType, 0);
          javaComponents.literal(keyType, 11);
          continue;
        case LONG:
          javaComponents.literal(keyType, 0L);
          continue;
        case SHORT:
          javaComponents.literal(keyType, (short) 0);
          continue;
        }
      }
      if (!key.Nonnull) {
        processedTypes.add(key.Type);
        javaComponents.nullLiteral(key.Type);
      }
    }
  }

}
