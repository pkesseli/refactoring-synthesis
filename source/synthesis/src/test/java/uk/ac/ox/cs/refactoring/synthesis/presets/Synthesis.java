package uk.ac.ox.cs.refactoring.synthesis.presets;

import java.io.IOException;
import java.util.NoSuchElementException;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoop;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.state.ClassLoaderClonerStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.StateFactory;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Reports;

public final class Synthesis {

  public static SnippetCandidate synthesise(final GeneratorConfiguration generatorConfiguration,
      final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchElementException, IOException {
    final StateFactory stateFactory = new ClassLoaderClonerStateFactory();
    final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);
    final Invoker invoker = new Invoker(methodToRefactor);
    try (final CegisLoopListener<SnippetCandidate> listener = Reports
        .createReportListener(methodToRefactor.FullyQualifiedClassName, methodToRefactor.MethodName)) {
      final CegisLoop<SnippetCandidate> cegis = new CegisLoop<>(executor, invoker, generatorConfiguration,
          SnippetCandidate.class, listener);
      return cegis.synthesise();
    }
  }

  private Synthesis() {
  }
}
