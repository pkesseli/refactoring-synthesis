package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        String constraintsList = IntStream.range(0, constraints.size())
            .mapToObj(i -> String.format("\t%d. %s", i + 1, constraints.get(i)))
            .collect(Collectors.joining("\n"));
        return context + "\n" + instruction + "\n\nTake the following constraints into consideration:\n"
                + TextTagger.tag("list", constraintsList)
                + extraInformation + "\n";
    }
}
