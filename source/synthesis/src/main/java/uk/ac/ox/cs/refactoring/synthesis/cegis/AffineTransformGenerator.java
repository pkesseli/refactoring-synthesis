package uk.ac.ox.cs.refactoring.synthesis.cegis;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.awt.geom.AffineTransform;

/** Preferred constructor for AffineTransform. */
public class AffineTransformGenerator extends Generator<AffineTransform> {

  /** {@link Generator#Generator(java.lang.Class)} */
  public AffineTransformGenerator() {
    super(AffineTransform.class);
  }

  @Override
  public AffineTransform generate(final SourceOfRandomness random, final GenerationStatus status) {
    final float m00 = random.nextFloat();
    final float m10 = random.nextFloat();
    final float m01 = random.nextFloat();
    final float m11 = random.nextFloat();
    final float m02 = random.nextFloat();
    final float m12 = random.nextFloat();
    return new AffineTransform(m00, m10, m01, m11, m02, m12);
  }
}
