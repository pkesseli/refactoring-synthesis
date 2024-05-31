package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** A structured representation of a prompt */
public class Prompt {
    public String context = "";
    public String instruction;
    public List<String> constraints = new ArrayList<>();
    public String extraInformation = "";

    public Prompt(String instruction) {
        this.instruction = instruction;
    }

    public Prompt(String context, String instruction) {
      this.context = context;
      this.instruction = instruction;
    }

    public Prompt(String context, String instruction, List<String> constraints) {
        this.context = context;
        this.instruction = instruction;
        this.constraints = constraints;
    }

    public String toString() {
      String constraintsList = constraints.stream().map(constraint -> String.format("  <constraint>\n    %s\n  </constraint>", constraint)).collect(Collectors.joining("\n"));
      return context + "\n" 
          + TextTagger.tag("instruction", instruction)
          + "\n\nTake the following constraints into consideration:\n"
          + TextTagger.tag("constraints-list", constraintsList)
          + extraInformation + "\n";
    }
}
