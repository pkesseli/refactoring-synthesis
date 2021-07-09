package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.util.List;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
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
   * @throws NoSuchMethodException  {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   */
  public static GeneratorConfiguration deprecatedMethod(final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, NoSuchMethodException {
    return deprecatedMethod(methodToRefactor, ClassLoaders.createIsolated(), (byte) 1, new NullSeed());
  }

  /**
   * Deprecated methods configuration with JavaDoc seed.
   * 
   * @param methodToRefactor {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @return {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws ClassNotFoundException {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   * @throws NoSuchMethodException  {@link #deprecatedMethod(MethodIdentifier, ClassLoader, byte, InstructionSetSeed)}
   */
  public static GeneratorConfiguration deprecatedMethodWithJavaDoc(final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, NoSuchMethodException {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    return deprecatedMethod(methodToRefactor, classLoader, (byte) 3, new JavaDocSeed(classLoader));
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
   * @throws NoSuchMethodException  {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   */
  private static GeneratorConfiguration deprecatedMethod(final MethodIdentifier methodToRefactor,
      final ClassLoader classLoader, final byte minInstructions, final InstructionSetSeed extraSeed)
      throws ClassNotFoundException, NoSuchMethodException {
    final ComponentDirectory components = new ComponentDirectory();
    seed(components, new TypeSeed(classLoader, methodToRefactor), new SignatureSeed(classLoader, methodToRefactor),
        extraSeed, new ConstantSeed(), new StatementSeed());
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
   * @throws ClassNotFoundException if reflectively accessing a class for analysis
   *                                fails.
   * @throws NoSuchMethodException  if reflecitvely accessing a method for
   *                                analysis fails.
   */
  private static void seed(final ComponentDirectory components, final InstructionSetSeed... instructionSetSeeds)
      throws ClassNotFoundException, NoSuchMethodException {
    for (final InstructionSetSeed instructionSetSeed : instructionSetSeeds)
      instructionSetSeed.seed(components);
  }
}
