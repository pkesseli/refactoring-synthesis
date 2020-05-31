package uk.ac.ox.cs.refactoring.solver.engine;

import org.junit.jupiter.api.Test;

public class Z3SolverTest {
  @Test
  void testSolve() {
    final Z3Solver solver = new Z3Solver();
    solver.solve();
  }
}
