package uk.ac.ox.cs.refactoring.synthesis.cegis;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.awt.TextArea;

/** Factory {@link TextArea} counterexamples. */
public class TextAreaGenerator extends Generator<TextArea> {

  /** Used to generate Strings. */
  private final GeneratorRepository repository;

  /**
   * {@link Generator#Generator(java.lang.Class)}
   * 
   * @param repository {@link #repository}
   */
  public TextAreaGenerator(final GeneratorRepository repository) {
    super(TextArea.class);
    this.repository = repository;
  }

  @Override
  public TextArea generate(final SourceOfRandomness random, final GenerationStatus status) {
    return new TextArea(repository.type(String.class).generate(random, status));
  }

  @Override
  public Generator<TextArea> copy() {
    return new TextAreaGenerator(repository);
  }
}
