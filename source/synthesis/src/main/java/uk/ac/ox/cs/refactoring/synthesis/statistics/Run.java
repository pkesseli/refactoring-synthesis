package uk.ac.ox.cs.refactoring.synthesis.statistics;

public class Run {

  public boolean FoundCodeHints;

  public long RuntimeInMilliseconds;

  public int Rounds;

  public FuzzedInputs Counterexamples = new FuzzedInputs();

  public FuzzedInputs Candidates = new FuzzedInputs();

  public String Solution;

  public boolean Unsound;
}
