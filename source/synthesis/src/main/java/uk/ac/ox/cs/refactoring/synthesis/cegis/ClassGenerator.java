package uk.ac.ox.cs.refactoring.synthesis.cegis;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

@SuppressWarnings("rawtypes")
public class ClassGenerator extends Generator<Class> {

  public ClassGenerator() {
    super(Class.class);
  }

  @Override
  public Class generate(final SourceOfRandomness random, final GenerationStatus status) {
    return Object.class;
  }
}
