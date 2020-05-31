package uk.ac.ox.cs.refactoring.solver.engine;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Optimize.Handle;
import com.microsoft.z3.Status;

public class Z3Solver {
  public void solve() {
    try(final Context context = new Context()) {
      final Optimize optimize = context.mkOptimize();
      final BitVecExpr x = context.mkBVConst("x", 8);
      final BitVecExpr y = context.mkBVConst("y", 8);

      optimize.Add(context.mkBVULT(x, context.mkBV(3, 8)),
          context.mkBVULT(y, context.mkBV(4, 8)));

      final Handle first = optimize.MkMaximize(x);
      final Handle second = optimize.MkMaximize(y);
      final Status status = optimize.Check();
      System.err.println("Status: " + status);
      System.err.println("Result x: " + first);
      System.err.println("Result y: " + second);
    }
  }
}
