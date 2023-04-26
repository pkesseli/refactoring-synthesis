package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Seed to add all polymorphic ancestors of existing components, so that we can
 * invoke matching methods. Unused types will later be removed by
 * {@link UnusedSideEffectFree}.
 */
public class AncestorSeed implements InstructionSetSeed {

  /**
   * {@link TypeFactory#create(com.github.javaparser.JavaParser, com.github.javaparser.resolution.types.ResolvedType)}
   */
  private final ParserContext parserContext;

  /**
   * Keep track of inserted types for potential later removal by
   * {@link UnusedSideEffectFree}.
   */
  private final Map<JavaLanguageKey, Component<JavaLanguageKey, ?>> insertedComponents;

  /** @param {@link #parserContext} */
  public AncestorSeed(final ParserContext parserContext,
      final Map<JavaLanguageKey, Component<JavaLanguageKey, ?>> insertedComponents) {
    this.parserContext = parserContext;
    this.insertedComponents = insertedComponents;
  }

  @Override
  public void seed(final ComponentDirectory components)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {

    final Set<JavaLanguageKey> keys = components.keySet(JavaLanguageKey.class).stream()
        .filter(key -> key.Type.isClassOrInterfaceType()).collect(Collectors.toSet());
    for (final JavaLanguageKey key : keys) {
      final List<Component<JavaLanguageKey, ?>> subtypeComponents = components.get(key).collect(Collectors.toList());
      final ClassOrInterfaceType type = (ClassOrInterfaceType) key.Type;
      final String qualifiedTypeName = type.getNameWithScope();
      final ResolvedReferenceTypeDeclaration resolvedReferenceType = parserContext.TypeSolver
          .solveType(qualifiedTypeName);
      for (final ResolvedReferenceType resolvedAncestorType : resolvedReferenceType.getAllAncestors()) {
        final Type ancestorType = TypeFactory.create(parserContext.JavaParser, resolvedAncestorType);
        final JavaLanguageKey ancestorKey = JavaLanguageKeys.duplicate(key, ancestorType);
        for (final Component<JavaLanguageKey, ?> component : subtypeComponents) {
          components.put(ancestorKey, component);
          insertedComponents.put(ancestorKey, component);
        }
      }
    }
  }
}
