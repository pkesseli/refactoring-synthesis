package uk.ac.ox.cs.refactoring.synthesis.statistics;

import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

public class GPTVerificationListener<Candidate> implements CegisLoopListener<Candidate> {

    @Override
    public void initial(Candidate candidate) {
    }

    @Override
    public void spurious(Candidate candidate) {
    }

    @Override
    public void genuine(Candidate candidate) {
    }

    @Override
    public void spurious(Counterexample counterexample) {
    }

    @Override
    public void genuine(Counterexample counterexample, ExecutionResult expected, ExecutionResult actual) {
        System.out.println(counterexample.toString() + ", expected: " + expected.Value.toString() + ", actual: " + actual.Value.toString());
    }

    @Override
    public void verified(Candidate candidate) {
    }

    @Override
    public void close() {
    }
    
}
