package uk.ac.ox.cs.refactoring.synthesis.cegis;

import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.ObjenesisStateFactory;

public class CegisLoopTest {
  @Test
  void plus() throws Exception {
    final IStateFactory stateFactory = new ObjenesisStateFactory();
    final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);
    final Invoker invoker = new Invoker(Benchmarks.DOUBLE_SUM, "plus",
        Arrays.asList(double.class.getName(), double.class.getName()));
    final CegisLoop<SnippetCandidate> cegis = new CegisLoop<SnippetCandidate>(executor, invoker,
        Arrays.asList(double.class, double.class), SnippetCandidate.class);

    final AssertionFailedError noCandidate = new AssertionFailedError();
    /**
     * With the default fuzzing configuration we have about a 10% chance of finding
     * a candidate. This loop should be removed once we use proper fuzzing guidance.
     */
    SnippetCandidate candidate = null;
    for (int i = 0; candidate == null && i < 10; ++i) {
      try {
        candidate = cegis.synthesise();
      } catch (final NoSuchElementException e) {
        noCandidate.addSuppressed(e);
      }
    }

    if (candidate == null) {
      throw noCandidate;
    }

    final List<String> actual = candidate.Block.toNode().toString().lines().map(String::trim)
        .collect(Collectors.toList());
    final Matcher<Iterable<String>> matcher = hasItems(equalTo("{"),
        either(equalTo("param1 + param0;")).or(equalTo("param0 + param1;")), equalTo("}"));
    MatcherAssert.assertThat(actual, matcher);
  }
}
