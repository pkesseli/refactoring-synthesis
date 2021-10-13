package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ConstructorComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

/**
 * Adds expression statements for all expressions in the instruction set.
 */
public class StatementSeed implements InstructionSetSeed {

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final Set<JavaLanguageKey> keys = components.keySet(JavaLanguageKey.class);
    final JavaComponents javaComponents = new JavaComponents(components);
    final Set<Type> insertedTypes = new HashSet<>();
    for (final JavaLanguageKey key : keys) {
      if (!key.Nonnull)
        continue;
      final Type type = key.Type;
      if (insertedTypes.contains(type))
        continue;
      insertedTypes.add(type);
      final JavaLanguageKey expressionKey = JavaLanguageKeys.nonnull(type);
      javaComponents.statement(type,
          new ConstructorComponent<>(Arrays.asList(expressionKey), ExpressionStatement.class));
    }
  }

}
