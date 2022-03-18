package uk.ac.ox.cs.refactoring.synthesis.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

class Parameters {
  
  static Path[] getJsonFiles(final String[] args) {
    return Arrays.stream(args).map(Paths::get).filter(Files::exists).toArray(Path[]::new);
  }
}
