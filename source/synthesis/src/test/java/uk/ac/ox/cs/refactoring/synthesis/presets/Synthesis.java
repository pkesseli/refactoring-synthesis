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
    final CegisLoopListener<SnippetCandidate> listener = Reports
        .createJsonReportListener(methodToRefactor.FullyQualifiedClassName, methodToRefactor.MethodName);
    final StateFactory stateFactory = new ClassLoaderClonerStateFactory();
    final SnippetCandidateExecutor executor = new SnippetCandidateExecutor(stateFactory);
    final Invoker invoker = new Invoker(methodToRefactor);
    final CegisLoop<SnippetCandidate> cegis = new CegisLoop<>(executor, invoker, generatorConfiguration,
        SnippetCandidate.class, listener);
    final SnippetCandidate tmp = cegis.synthesise();
    System.err.println(tmp);
    return tmp;
  }

  private Synthesis() {
  }
}
