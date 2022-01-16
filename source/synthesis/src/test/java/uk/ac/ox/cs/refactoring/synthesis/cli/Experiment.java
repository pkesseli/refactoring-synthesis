package uk.ac.ox.cs.refactoring.synthesis.cli;

import picocli.CommandLine;

public class Experiment {

  public static void main(final String[] args) {
    System.exit(new CommandLine(new ExperimentCommand()).execute(args));
  }
}
