package uk.ac.ox.cs.refactoring.synthesis.cegis;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/** Factory {@link TextArea} counterexamples. */
public class JTextComponentGenerator extends Generator<JTextComponent> {

  /** Used to generate Strings. */
  private final GeneratorRepository repository;

  /**
   * {@link Generator#Generator(java.lang.Class)}
   * 
   * @param repository {@link #repository}
   */
  public JTextComponentGenerator(final GeneratorRepository repository) {
    super(JTextComponent.class);
    this.repository = repository;
  }

  @Override
  public JTextComponent generate(final SourceOfRandomness random, final GenerationStatus status) {
    final String text = repository.type(String.class).generate(random, status);
    final int columns = Math.abs(repository.type(int.class).generate(random, status));
    return new JTextField(text, columns);
  }

  @Override
  public Generator<JTextComponent> copy() {
    return new JTextComponentGenerator(repository);
  }
}
