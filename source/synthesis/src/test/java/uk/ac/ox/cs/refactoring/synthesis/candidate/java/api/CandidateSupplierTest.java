package uk.ac.ox.cs.refactoring.synthesis.candidate.java.api;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import com.github.javaparser.ast.type.PrimitiveType;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.junit.Test;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.FieldAccess;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.statement.ExpressionStatement;

public class CandidateSupplierTest {
  static double actual = 31.5;

  @Test
  public void field() throws Exception {
    final FieldAccess field = new FieldAccess("actual", CandidateSupplierTest.class.getName(),
        PrimitiveType.doubleType());
    final SourceOfRandomness sourceOfRandomness = mock(SourceOfRandomness.class);
    when(sourceOfRandomness.nextByte(anyByte(), anyByte())).thenReturn((byte) 1);
    when(sourceOfRandomness.nextInt(anyInt(), anyInt())).thenAnswer(i -> i.getArgument(0));
    final CandidateSupplier candidateSupplier = new CandidateSupplier(null, Collections.emptyList(),
        Arrays.asList(field), sourceOfRandomness);
    final SnippetCandidate snippetCandidate = candidateSupplier.get();
    assertThat(snippetCandidate.Block.Statements, hasItems(isA(ExpressionStatement.class)));
  }
}
