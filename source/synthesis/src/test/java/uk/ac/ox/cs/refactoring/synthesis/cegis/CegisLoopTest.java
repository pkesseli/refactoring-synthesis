package uk.ac.ox.cs.refactoring.synthesis.cegis;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.ExecutionContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.ObjenesisStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

public class CegisLoopTest {
  @Test
  void plus() throws Exception {
    final IStateFactory stateFactory = new ObjenesisStateFactory();
    final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);
    final Invoker invoker = new Invoker(Benchmarks.DOUBLE_SUM, "plus",
        Arrays.asList(double.class.getName(), double.class.getName()));
    final CegisLoop<SnippetCandidate> cegis = new CegisLoop<SnippetCandidate>(executor, invoker,
        Arrays.asList(double.class, double.class), SnippetCandidate.class);
    final SnippetCandidate candidate = cegis.synthesise();

    final State state = new State(null, 10.5, 22.5);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(33.0, (double) candidate.Block.execute(context), 0.000001);
  }
}
