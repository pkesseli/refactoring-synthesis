package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Builder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.NullaryComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.SizedBuilder;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.random.RandomnessAccessor;

/**
 * {@link Generator} for Java {@link SnippetCandidate}.
 */
public class SnippetCandidateGenerator extends Generator<SnippetCandidate> {

  /**
   * Settings for candidate generation.
   */
  private final GeneratorConfiguration generatorConfiguration;

  /**
   * Builder for single statements.
   */
  private final Builder<JavaLanguageKey, IStatement> statements;

  /**
   * @param generatorConfiguration {@link #generatorConfiguration}
   */
  public SnippetCandidateGenerator(final GeneratorConfiguration generatorConfiguration) {
    super(SnippetCandidate.class);
    this.generatorConfiguration = generatorConfiguration;
    statements = new SizedBuilder<>(generatorConfiguration.Components, generatorConfiguration.MaxInstructionLength);
  }

  @Override
  public SnippetCandidate generate(final SourceOfRandomness random, final GenerationStatus status) {
    final List<JavaLanguageKey> allTypes = generatorConfiguration.Components.keySet(JavaLanguageKey.class).stream()
        .filter(k -> IStatement.class == k.Kind).collect(Collectors.toList());
    final JavaLanguageKey resultKey = JavaLanguageKeys.statement(TypeFactory.create(generatorConfiguration.ResultType));
    final ComponentDirectory temporaryVariables = new ComponentDirectory();
    final SnippetCandidate result = new SnippetCandidate();
    final byte size = RandomnessAccessor.nextByte(random, generatorConfiguration.MinInstructions,
        generatorConfiguration.MaxInstructions);
    for (byte i = 0; i < size; ++i) {
      final List<JavaLanguageKey> allowedTypes;
      if (i == size - 1) {
        allowedTypes = Arrays.asList(resultKey);
      } else {
        allowedTypes = allTypes;
      }
      final IStatement statement = statements.build(random, allowedTypes, temporaryVariables);
      result.Block.Statements.add(statement);

      final Optional<IExpression> symbol = statement.getSymbolExpression();
      if (symbol.isPresent()) {
        final IExpression expression = symbol.get();
        final JavaLanguageKey key = JavaLanguageKeys.nonnull(expression.getType());
        temporaryVariables.put(key, new NullaryComponent<>(expression));
      }
    }

    return result;
  }

  @Override
  public Generator<SnippetCandidate> copy() {
    return new SnippetCandidateGenerator(generatorConfiguration);
  }

  public static class TestClass {
    /**
     * Placeholder for {@link Generator}.
     * 
     * @param candidate ignored.
     * @return Reflective reference to this method.
     */
    @Fuzz
    public static Method getFrameworkMethodPlaceholder(final SnippetCandidate candidate) {
      try {
        return TestClass.class.getDeclaredMethod("getFrameworkMethodPlaceholder", SnippetCandidate.class);
      } catch (final NoSuchMethodException | SecurityException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
