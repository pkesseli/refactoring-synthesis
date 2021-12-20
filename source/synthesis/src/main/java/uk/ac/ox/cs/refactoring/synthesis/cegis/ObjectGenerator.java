package uk.ac.ox.cs.refactoring.synthesis.cegis;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Our counterexample and candidate generators are allowed to use JQF
 * recursively to populate fields of objects they generate. If one of those
 * field types are {@link Object}, the counterexample and candidate generators
 * become eligible themselves to generate an object of type {@link Object},
 * namely their counterexample or candidate type. This would lead to an infinite
 * recursion though, which is why we register an explicit object factory.
 */
public class ObjectGenerator extends Generator<Object> {

  public ObjectGenerator() {
    super(Object.class);
  }

  @Override
  public Object generate(final SourceOfRandomness random, final GenerationStatus status) {
    return new Object();
  }
}
