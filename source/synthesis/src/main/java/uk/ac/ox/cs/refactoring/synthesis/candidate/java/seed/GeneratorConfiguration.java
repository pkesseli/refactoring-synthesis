package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.util.List;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;

public interface GeneratorConfiguration {
  List<JavaLanguageKey> getAllowedTypes(int instructionIndex);

  ComponentDirectory getComponents();

  int getNumberOfInstructions();
}
