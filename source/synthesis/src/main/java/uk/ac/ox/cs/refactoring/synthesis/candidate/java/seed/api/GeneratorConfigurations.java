package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api;

import java.util.List;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.ConstantSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.ConsumerSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.FactorySeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.InstructionSetSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.SignatureSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.StatementSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.TypeSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.JavaDocSeed;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;

/**
 * Factory for {@link GeneratorConfiguration}s.
 */
public final class GeneratorConfigurations {

  private GeneratorConfigurations() {
  }

  /**
   * Default deprecated methods configuration.
   * 
   * @param methodToRefactor {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @return {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws ClassNotFoundException {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws IllegalAccessException {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws NoSuchFieldException   {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws NoSuchMethodException  {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   */
  public static GeneratorConfiguration deprecatedMethod(final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final ComponentDirectory components = new ComponentDirectory();
    seed(components, new TypeSeed(classLoader, methodToRefactor), new SignatureSeed(classLoader, methodToRefactor),
        new ConstantSeed(), new StatementSeed());
    return deprecatedMethod(methodToRefactor, classLoader, components, (byte) 1);
  }

  /**
   * Deprecated methods configuration with JavaDoc seed.
   * 
   * @param methodToRefactor {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @return {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws ClassNotFoundException {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws IllegalAccessException {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws NoSuchFieldException   {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws NoSuchMethodException  {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   */
  public static GeneratorConfiguration deprecatedMethodWithJavaDoc(final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final ComponentDirectory components = new ComponentDirectory();
    seed(components, new JavaDocSeed(classLoader, methodToRefactor), new SignatureSeed(classLoader, methodToRefactor),
        new FactorySeed(classLoader), new ConsumerSeed(classLoader), new StatementSeed());
    return deprecatedMethod(methodToRefactor, classLoader, components, (byte) 3);
  }

  /**
   * Provides a deprecated methods configuration.
   * 
   * @param methodToRefactor Deprecated method to refactor.
   * @param classLoader      Class loader to use when loading classes reflectively
   *                         to inspect prameter types.
   * @param minInstructions  Minimum program size.
   * @param extraSeed        Additional {@link InstructionSetSeed} to apply.
   * @return Candidate generator configuration.
   * @throws ClassNotFoundException {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws IllegalAccessException {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws NoSuchFieldException   {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws NoSuchMethodException  {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   */
  private static GeneratorConfiguration deprecatedMethod(final MethodIdentifier methodToRefactor,
      final ClassLoader classLoader, final ComponentDirectory components, final byte minInstructions)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final byte maxInstructions = 3;
    final byte maxInstructionLength = 3;
    final Invokable invokable = Methods.create(classLoader, methodToRefactor);
    final Class<?> instanceType = invokable.getInstanceType();
    final List<Class<?>> parameterTypes = invokable.getParameterTypes();
    final Class<?> resultType = invokable.getReturnType();
    return new GeneratorConfiguration(components, minInstructions, maxInstructions, maxInstructionLength, instanceType,
        parameterTypes, resultType);
  }

  /**
   * Composite method for instruction set seeds.
   * 
   * @param components          {@link ComponentDirectory} to seed.
   * @param instructionSetSeeds All seeds to apply.
   * @throws ClassNotFoundException {@link InstructionSetSeed#seed(ComponentDirectory)}
   * @throws IllegalAccessException {@link InstructionSetSeed#seed(ComponentDirectory)}
   * @throws NoSuchFieldException   {@link InstructionSetSeed#seed(ComponentDirectory)}
   * @throws NoSuchMethodException  {@link InstructionSetSeed#seed(ComponentDirectory)}
   */
  private static void seed(final ComponentDirectory components, final InstructionSetSeed... instructionSetSeeds)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    for (final InstructionSetSeed instructionSetSeed : instructionSetSeeds)
      instructionSetSeed.seed(components);
  }
}
