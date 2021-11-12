package uk.ac.ox.cs.refactoring.synthesis.cegis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfigurations;
import uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation;
import uk.ac.ox.cs.refactoring.synthesis.presets.Synthesis;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class CegisLoopTest {

  @Test
  void doublePlus() throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Benchmarks.DOUBLE_SUM, "plus",
        Arrays.asList("double", "double"));
    final SnippetCandidate candidate = Synthesis.synthesise(GeneratorConfigurations.deprecatedMethod(methodToRefactor),
        methodToRefactor);
    final State state = new State(null, 10.5, 22.5);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(33.0, (double) candidate.Block.execute(context), 0.000001);
  }

  @Test
  void intPlus() throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Benchmarks.INTEGER_SUM, "plus",
        Arrays.asList("int", "int"));
    final SnippetCandidate candidate = Synthesis.synthesise(GeneratorConfigurations.deprecatedMethod(methodToRefactor),
        methodToRefactor);
    final State state = new State(null, 10, 22);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(32, (int) candidate.Block.execute(context));
  }

  @ParameterizedTest
  @CsvSource({ "getHours," + Calendar.HOUR_OF_DAY, "getMinutes," + Calendar.MINUTE, "getSeconds," + Calendar.SECOND })
  void javaUtilDateGet(final String methodName, final int field) throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Date.class.getName(), methodName,
        Collections.emptyList());
    final SnippetCandidate candidate = Deprecation.synthesise(methodToRefactor);
    final Calendar calendar = Calendar.getInstance();
    final Date date = calendar.getTime();
    final int expected = calendar.get(field);
    final State state = new State(date);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(expected, candidate.Block.execute(context));
  }

  @Test
  void javaAwtRectangle() throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier("java.awt.Rectangle", "inside",
        Arrays.asList("int", "int"));
    final SnippetCandidate candidate = Deprecation.synthesise(methodToRefactor);

    assertThat(candidate.toString(), containsString(".contains("));
  }
}
