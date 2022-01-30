package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.awt.FontMetrics;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.mockito.Mockito;

/** Preferred constructor for AffineTransform. */
public class FontMetricsGenerator extends Generator<FontMetrics> {

  /** {@link Generator#Generator(java.lang.Class)} */
  public FontMetricsGenerator() {
    super(FontMetrics.class);
  }

  @Override
  public FontMetrics generate(final SourceOfRandomness random, final GenerationStatus status) {
    final FontMetrics fontMetrics = Mockito.mock(FontMetrics.class,
        Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    Mockito.when(fontMetrics.getMaxDescent()).thenReturn(random.nextInt());
    return fontMetrics;
  }
}
