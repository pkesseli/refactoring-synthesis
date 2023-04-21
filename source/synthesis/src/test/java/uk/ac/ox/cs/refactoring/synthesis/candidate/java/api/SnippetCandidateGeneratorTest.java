package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.classmate.TypeResolver;
import com.github.javaparser.ast.type.PrimitiveType;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.FieldSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.StatementSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

class SnippetCandidateGeneratorTest {
  static double actual = 31.5;

  @Test
  void field() throws Exception {
    final FieldAccess field = new FieldAccess("actual", SnippetCandidateGeneratorTest.class.getName(),
        PrimitiveType.doubleType());
    final SourceOfRandomness sourceOfRandomness = mock(SourceOfRandomness.class);
    when(sourceOfRandomness.nextByte(anyByte(), anyByte())).thenReturn((byte) 1);
    when(sourceOfRandomness.nextInt(anyInt(), anyInt())).thenAnswer(i -> i.getArgument(0));
    final ComponentDirectory components = new ComponentDirectory();
    new FieldSeed(Arrays.asList(field)).seed(components);
    new StatementSeed().seed(components);
    final byte minInstructions = 1;
    final byte maxInstructions = 1;
    final byte maxInstructionLength = 1;
    final TypeResolver typeResolver = new TypeResolver();
    final GeneratorConfiguration generatorConfiguration = new GeneratorConfiguration(components, minInstructions,
        maxInstructions, maxInstructionLength, false, typeResolver.resolve(Void.class), Collections.emptyList(),
        typeResolver.resolve(double.class), false, 10, 100, 1, 400);
    final SnippetCandidateGenerator candidateSupplier = new SnippetCandidateGenerator(generatorConfiguration);
    final SnippetCandidate snippetCandidate = candidateSupplier.generate(sourceOfRandomness, null);
    assertThat(snippetCandidate.Block.Statements, hasItems(isA(ExpressionStatement.class)));
  }
}