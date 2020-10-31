package uk.ac.ox.cs.refactoring.jqfexperiments;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class StringGenerator extends Generator<String> {

  public StringGenerator() {
    super(String.class);
  }

  @Override
  public String generate(final SourceOfRandomness random, final GenerationStatus status) {
    final int length = random.nextInt(10);
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; ++i) {
      sb.append(random.nextChar('a', 'z'));
    }
    return sb.toString();
  }
}
