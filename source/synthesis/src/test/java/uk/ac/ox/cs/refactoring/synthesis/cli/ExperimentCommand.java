package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfigurations;
import uk.ac.ox.cs.refactoring.synthesis.experiment.java_util_DateTest;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Report;
import uk.ac.ox.cs.refactoring.synthesis.statistics.Reports;

@Command(name = "experiment")
class ExperimentCommand implements Callable<Integer> {

  @Option(names = "--repetitions", defaultValue = "1")
  private int repetitions;

  @Option(names = "--json-report-file-path", defaultValue = "./report.json")
  private Path jsonReportFilePath;

  @Option(names = "--use-random-guidance", defaultValue = "false")
  private boolean useRandomGuidance;

  @Option(names = "--stage-one-max-inputs", defaultValue = "100")
  private long stage1MaxInputs;

  @Option(names = "--stage-one-max-counterexamples", defaultValue = "10")
  private long stage1MaxCounterexamples;

  @Option(names = "--stage-two-max-inputs", defaultValue = "400")
  private long stage2MaxInputs;

  @Option(names = "--stage-two-max-counterexamples", defaultValue = "1")
  private long stage2MaxCounterexamples;

  @Override
  public Integer call() throws Exception {
    final LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
        .selectors(DiscoverySelectors.selectPackage(java_util_DateTest.class.getPackageName())).build();
    final Launcher launcher = LauncherFactory.create();
    final Report report = Reports.DEFAULT_REPORT;
    final SynthesisResultListener listener = new SynthesisResultListener(report);

    System.setProperty(GeneratorConfigurations.USE_RANDOM_GUIDANCE, Boolean.toString(useRandomGuidance));
    System.setProperty(GeneratorConfigurations.STAGE_1_MAX_INPUTS, Long.toString(stage1MaxInputs));
    System.setProperty(GeneratorConfigurations.STAGE_1_MAX_COUNTEREXAMPLES, Long.toString(stage1MaxCounterexamples));
    System.setProperty(GeneratorConfigurations.STAGE_2_MAX_INPUTS, Long.toString(stage2MaxInputs));
    System.setProperty(GeneratorConfigurations.STAGE_2_MAX_COUNTEREXAMPLES, Long.toString(stage1MaxCounterexamples));

    for (int i = 0; i < repetitions; ++i) {
      launcher.execute(request, listener);
    }

    final ObjectMapper objectMapper = new ObjectMapper();
    final ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
    try (final OutputStream os = Files.newOutputStream(jsonReportFilePath)) {
      writer.writeValue(os, report);
    } catch (final IOException e) {
      e.printStackTrace();
      return ExitCode.SOFTWARE;
    }
    return ExitCode.OK;
  }
}
