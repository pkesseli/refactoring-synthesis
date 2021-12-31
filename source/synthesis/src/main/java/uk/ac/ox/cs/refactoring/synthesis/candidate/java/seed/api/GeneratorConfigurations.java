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

/** Factory for {@link GeneratorConfiguration}s. */
public final class GeneratorConfigurations {

  /** System property configuring whether to use no guidance. */
  public static final String USE_RANDOM_GUIDANCE = "resynth.fuzzing.random";

  /**
   * System property configuring maximum number of counterexamples during the
   * first verification stage.
   */
  public static final String STAGE_1_MAX_COUNTEREXAMPLES = "resynth.verification.stage1.maxCounterexamples";

  /**
   * System property configuring maximum number of inputs during the first
   * verification stage.
   */
  public static final String STAGE_1_MAX_INPUTS = "resynth.verification.stage1.maxInputs";

  /**
   * System property configuring maximum number of counterexamples during the
   * second verification stage.
   */
  public static final String STAGE_2_MAX_COUNTEREXAMPLES = "resynth.verification.stage2.maxCounterexamples";

  /**
   * System property configuring maximum number of inputs during the second
   * verification stage.
   */
  public static final String STAGE_2_MAX_INPUTS = "resynth.verification.stage2.maxInputs";

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
    return deprecatedMethod(methodToRefactor, classLoader, components, (byte) 1, false, 100, 10, 400, 1);
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

    final boolean useRandomGuidance = Boolean.getBoolean(USE_RANDOM_GUIDANCE);
    final long stage1MaxCounterexamples = Long.getLong(STAGE_1_MAX_COUNTEREXAMPLES, 10);
    final long stage1MaxInputs = Long.getLong(STAGE_1_MAX_INPUTS, 100);
    final long stage2MaxCounterexamples = Long.getLong(STAGE_2_MAX_COUNTEREXAMPLES, 1);
    final long stage2MaxInputs = Long.getLong(STAGE_2_MAX_INPUTS, 400);
    return deprecatedMethod(methodToRefactor, classLoader, components, (byte) 3, useRandomGuidance,
        stage1MaxCounterexamples, stage1MaxInputs, stage2MaxCounterexamples, stage2MaxInputs);
  }

  /**
   * Provides a deprecated methods configuration.
   * 
   * @param methodToRefactor         Deprecated method to refactor.
   * @param classLoader              Class loader to use when loading classes
   *                                 reflectively
   *                                 to inspect prameter types.
   * @param minInstructions          Minimum program size.
   * @param useRandomGuidance        {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, Class, List, Class, boolean, long, long, long, long)}
   * @param stage1MaxCounterexamples {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, Class, List, Class, boolean, long, long, long, long)}
   * @param stage1MaxInputs          {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, Class, List, Class, boolean, long, long, long, long)}
   * @param stage2MaxCounterexamples {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, Class, List, Class, boolean, long, long, long, long)}
   * @param stage2MaxInputs          {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, Class, List, Class, boolean, long, long, long, long)}
   * @return Candidate generator configuration.
   * @throws ClassNotFoundException {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws IllegalAccessException {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws NoSuchFieldException   {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws NoSuchMethodException  {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   */
  private static GeneratorConfiguration deprecatedMethod(final MethodIdentifier methodToRefactor,
      final ClassLoader classLoader, final ComponentDirectory components, final byte minInstructions,
      final boolean useRandomGuidance,
      final long stage1MaxCounterexamples, final long stage1MaxInputs, final long stage2MaxCounterexamples,
      final long stage2MaxInputs)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final byte maxInstructions = 3;
    final byte maxInstructionLength = 3;
    final Invokable invokable = Methods.create(classLoader, methodToRefactor);
    final Class<?> instanceType = invokable.getInstanceType();
    final List<Class<?>> parameterTypes = invokable.getParameterTypes();
    final Class<?> resultType = invokable.getReturnType();
    return new GeneratorConfiguration(components, minInstructions, maxInstructions, maxInstructionLength, instanceType,
        parameterTypes, resultType, useRandomGuidance, stage1MaxCounterexamples, stage1MaxInputs,
        stage2MaxCounterexamples, stage2MaxInputs);
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

  private GeneratorConfigurations() {
  }
}
