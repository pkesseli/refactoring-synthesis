package uk.ac.ox.cs.refactoring.synthesis.cegis;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.CounterexampleGenerator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;

/** Preferred constructor for AWT components. */
public class ComponentGenerator extends Generator<Component> {

  private final TypeResolver typeResolver = new TypeResolver();

  private final MemberResolver memberResolver = new MemberResolver(typeResolver);

  /** {@link Generator#Generator(java.lang.Class)} */
  public ComponentGenerator() {
    super(Component.class);
  }

  @Override
  public Component generate(final SourceOfRandomness random, final GenerationStatus status) {
    final ScrollPane scrollPane = new ScrollPane(random.nextInt(0, 2));
    scrollPane.setEnabled(random.nextBoolean());
    scrollPane.setVisible(random.nextBoolean());
    if (random.nextBoolean())
      scrollPane.setFocusable(random.nextBoolean());
    scrollPane.setBounds(random.nextInt(1, Integer.MAX_VALUE), random.nextInt(1, Integer.MAX_VALUE),
        random.nextInt(1, Integer.MAX_VALUE), random.nextInt(1, Integer.MAX_VALUE));
    scrollPane.setMinimumSize(
        new Dimension(random.nextInt(1, scrollPane.getWidth()), random.nextInt(1, scrollPane.getHeight())));
    scrollPane.setPreferredSize(
        new Dimension(random.nextInt(1, scrollPane.getWidth()), random.nextInt(1, scrollPane.getHeight())));

    final ResolvedType resolvedType = typeResolver.resolve(ScrollPane.class);
    try {
      CounterexampleGenerator.postProcess(memberResolver, resolvedType, scrollPane);
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    return scrollPane;
  }
}
