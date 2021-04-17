package uk.ac.ox.cs.refactoring.synthesis.cegis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
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

  private final IStateFactory stateFactory = new ObjenesisStateFactory();

  private final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);

  @Test
  void plus() throws Exception {
    final SnippetCandidate candidate = synthesise(Benchmarks.DOUBLE_SUM, "plus", double.class, double.class);
    final State state = new State(null, 10.5, 22.5);
    final ExecutionContext context = new ExecutionContext(CegisLoopTest.class.getClassLoader(), state);
    assertEquals(33.0, (double) candidate.Block.execute(context), 0.000001);
  }

  @Test
  void date() throws Exception {
    // final SnippetCandidate candidate = synthesise(Date.class.getName(), "<init>",
    // int.class, int.class, int.class);
    Assertions.fail();
  }

  private SnippetCandidate synthesise(final String fullyQualifiedClassName, final String methodName,
      final Class<?>... parameterTypes)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, IOException {
    return synthesise(fullyQualifiedClassName, methodName, Collections.emptyList(), parameterTypes);
  }

  private SnippetCandidate synthesise(final String fullyQualifiedClassName, final String methodName,
      final Iterable<Method> methods, final Class<?>... parameterTypes)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, IOException {
    final List<String> fullyQualifiedParameterTypeNames = Arrays.stream(parameterTypes).map(Class::getName)
        .collect(Collectors.toList());
    final Invoker invoker = new Invoker(fullyQualifiedClassName, methodName, fullyQualifiedParameterTypeNames);
    final CegisLoop<SnippetCandidate> cegis = new CegisLoop<>(executor, invoker, Arrays.asList(parameterTypes), methods,
        SnippetCandidate.class);
    return cegis.synthesise();
  }

}
