package uk.ac.ox.cs.refactoring.synthesis.cegis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.GeneratorConfigurations;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.ObjenesisStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class CegisLoopTest {

  private final IStateFactory stateFactory = new ObjenesisStateFactory();

  private final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);

  @Test
  void doublePlus() throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Benchmarks.DOUBLE_SUM, "plus",
        Arrays.asList("double", "double"));
    final SnippetCandidate candidate = synthesise(GeneratorConfigurations.deprecatedMethod(methodToRefactor),
        methodToRefactor);
    final State state = new State(null, 10.5, 22.5);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(33.0, (double) candidate.Block.execute(context), 0.000001);
  }

  @Test
  void intPlus() throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Benchmarks.INTEGER_SUM, "plus",
        Arrays.asList("int", "int"));
    final SnippetCandidate candidate = synthesise(GeneratorConfigurations.deprecatedMethod(methodToRefactor),
        methodToRefactor);
    final State state = new State(null, 10, 22);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(32, (int) candidate.Block.execute(context));
  }

  @Test
  void getHours() throws Exception {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(Date.class.getName(), "getHours",
        Collections.emptyList());
    final SnippetCandidate candidate = synthesise(GeneratorConfigurations.deprecatedMethodWithJavaDoc(methodToRefactor),
        methodToRefactor);
    final Calendar calendar = Calendar.getInstance();
    final Date date = calendar.getTime();
    final int expected = calendar.get(Calendar.HOUR_OF_DAY);
    final State state = new State(date);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(expected, candidate.Block.execute(context));
  }

  private SnippetCandidate synthesise(final GeneratorConfiguration generatorConfiguration,
      final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchElementException, IOException {
    final Invoker invoker = new Invoker(methodToRefactor);
    final CegisLoop<SnippetCandidate> cegis = new CegisLoop<>(executor, invoker, generatorConfiguration,
        SnippetCandidate.class);
    return cegis.synthesise();
  }

}
