package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.lang.invoke.MethodHandles;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/** Factory {@link MethodHandles.Lookup} counterexamples. */
public class MethodHandlesLookupGenerator extends Generator<MethodHandles.Lookup> {

  /** {@link Generator#Generator(java.lang.Class)} */
  public MethodHandlesLookupGenerator() {
    super(MethodHandles.Lookup.class);
  }

  @Override
  public MethodHandles.Lookup generate(final SourceOfRandomness random, final GenerationStatus status) {
    return random.nextBoolean() ? MethodHandles.publicLookup() : MethodHandles.lookup();
  }
}
