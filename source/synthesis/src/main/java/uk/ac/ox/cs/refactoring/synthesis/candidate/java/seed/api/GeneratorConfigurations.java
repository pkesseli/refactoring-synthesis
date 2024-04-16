package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMethod;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.Methods;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserContext;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.parser.ParserFactory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.AncestorSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.ConstantSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.ConsumerSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.FactorySeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.InstructionSetSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.NumericCastSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.SignatureSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.StatementSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.TypeSeed;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context.UnusedSideEffectFree;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc.JavaDocSeed;

/** Factory for {@link GeneratorConfiguration}s. */
public final class GeneratorConfigurations {

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

  /** System property configuring whether to hints from Javadoc. */
  public static final String USE_JAVADOC = "resynth.synthesis.javadoc";

  /** System property configuring whether to use no guidance. */
  public static final String USE_RANDOM_GUIDANCE = "resynth.fuzzing.random";

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
    return deprecatedMethod(methodToRefactor, classLoader, components, null, false, (byte) 1, false, 100, 10, 400, 1);
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
  public static GeneratorConfiguration experimentConfiguration(final MethodIdentifier methodToRefactor)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final ComponentDirectory components = new ComponentDirectory();

    final ParserContext parserContext = ParserFactory.create(classLoader);
    final boolean useJavaDoc = getBoolean(USE_JAVADOC, true);
    final JavaDocSeed javaDocSeed = new JavaDocSeed(parserContext, classLoader, methodToRefactor);
    final TypeSeed typeSeed = new TypeSeed(classLoader, methodToRefactor);
    final SignatureSeed signatureSeed = new SignatureSeed(classLoader, methodToRefactor);
    final ConstantSeed constantSeed = new ConstantSeed();
    final NumericCastSeed numericCastSeed = new NumericCastSeed(classLoader, methodToRefactor);
    final FactorySeed factorySeed = new FactorySeed(classLoader, constantSeed);
    final ConsumerSeed consumerSeed = new ConsumerSeed(classLoader);
    final StatementSeed statementSeed = new StatementSeed();
    final Map<JavaLanguageKey, Component<JavaLanguageKey, ?>> insertedComponents = new HashMap<>();
    final AncestorSeed ancestorSeed = new AncestorSeed(parserContext, insertedComponents);
    final UnusedSideEffectFree unusedSeed = new UnusedSideEffectFree(insertedComponents);

    boolean foundCodeHints = false;
    if (useJavaDoc) {
      seed(components, javaDocSeed);
      foundCodeHints = components.size() > 0;
      if (foundCodeHints)
        seed(components, signatureSeed, ancestorSeed, numericCastSeed, factorySeed, constantSeed, unusedSeed,
            consumerSeed, statementSeed);
    }
    if (components.size() == 0)
      seed(components, typeSeed, signatureSeed, constantSeed, statementSeed);

    final boolean useRandomGuidance = getBoolean(USE_RANDOM_GUIDANCE, false);
    final long stage1MaxCounterexamples = Long.getLong(STAGE_1_MAX_COUNTEREXAMPLES, 1);
    final long stage1MaxInputs = Long.getLong(STAGE_1_MAX_INPUTS, 500);
    final long stage2MaxCounterexamples = Long.getLong(STAGE_2_MAX_COUNTEREXAMPLES, 0);
    final long stage2MaxInputs = Long.getLong(STAGE_2_MAX_INPUTS, 0);
    return deprecatedMethod(methodToRefactor, classLoader, components, javaDocSeed, foundCodeHints, (byte) 3, useRandomGuidance,
        stage1MaxCounterexamples, stage1MaxInputs, stage2MaxCounterexamples, stage2MaxInputs);
  }

  /**
   * Equivalent to {@link Boolean#getBoolean(String)}, but allows to specify a
   * default value.
   * 
   * @param name         {@link Boolean#getBoolean(String)}
   * @param defaultValue Default value if no matching property exists.
   * @return {@link Boolean#getBoolean(String)} if the property exists,
   *         {@code defaultValue} otherwise.
   */
  private static boolean getBoolean(final String name, final boolean defaultValue) {
    final String value = System.getProperty(name);
    if (value == null)
      return defaultValue;
    return Boolean.parseBoolean(value);
  }

  /**
   * Provides a deprecated methods configuration.
   * 
   * @param methodToRefactor         Deprecated method to refactor.
   * @param classLoader              Class loader to use when loading classes
   *                                 reflectively
   *                                 to inspect prameter types.
   * @param foundCodeHints           {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, boolean, ResolvedType, List, ResolvedType, boolean, long, long, long, long)}
   * @param minInstructions          Minimum program size.
   * @param useRandomGuidance        {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, boolean, ResolvedType, List, ResolvedType, boolean, long, long, long, long)}
   * @param stage1MaxCounterexamples {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, boolean, ResolvedType, List, ResolvedType, boolean, long, long, long, long)}
   * @param stage1MaxInputs          {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, boolean, ResolvedType, List, ResolvedType, boolean, long, long, long, long)}
   * @param stage2MaxCounterexamples {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, boolean, ResolvedType, List, ResolvedType, boolean, long, long, long, long)}
   * @param stage2MaxInputs          {@link GeneratorConfiguration#GeneratorConfiguration(ComponentDirectory, byte, byte, byte, boolean, ResolvedType, List, ResolvedType, boolean, long, long, long, long)}
   * @return Candidate generator configuration.
   * @throws ClassNotFoundException {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws IllegalAccessException {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws NoSuchFieldException   {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   * @throws NoSuchMethodException  {@link #seed(ComponentDirectory, InstructionSetSeed...)}
   */
  private static GeneratorConfiguration deprecatedMethod(final MethodIdentifier methodToRefactor,
      final ClassLoader classLoader, final ComponentDirectory components, final JavaDocSeed javaDocSeed, final boolean foundCodeHints,
      final byte minInstructions, final boolean useRandomGuidance, final long stage1MaxCounterexamples,
      final long stage1MaxInputs, final long stage2MaxCounterexamples, final long stage2MaxInputs)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
    final byte maxInstructions = 3;
    final byte maxInstructionLength = 3;

    final Method method = Methods.getMethod(classLoader, methodToRefactor);
    if (!Boolean.parseBoolean("resynth.synthesis.javadoc") && Modifier.isAbstract(method.getModifiers())) {
      // Do not try to synthesise for abstract methods without code hints.
      throw new NoSuchElementException();
    }
    final TypeResolver typeResolver = new TypeResolver();
    final ResolvedType instanceType = typeResolver.resolve(method.getDeclaringClass());
    final MemberResolver memberResolver = new MemberResolver(typeResolver);
    final ResolvedTypeWithMembers typeWithMembers = memberResolver.resolve(instanceType, null, null);
    final ResolvedMethod[] methods;
    if (Modifier.isStatic(method.getModifiers()))
      methods = typeWithMembers.getStaticMethods();
    else
      methods = typeWithMembers.getMemberMethods();

    final ResolvedMethod resolvedMethod = Arrays.stream(methods).filter(new MethodFilter(method)).findAny().get();
    final List<ResolvedType> parameterTypes = IntStream.range(0, resolvedMethod.getArgumentCount())
        .mapToObj(resolvedMethod::getArgumentType).collect(Collectors.toList());
    final ResolvedType returnType = resolvedMethod.getReturnType();
    final ResolvedType resultType = returnType != null ? returnType : typeResolver.resolve(void.class);
    return new GeneratorConfiguration(javaDocSeed, components, minInstructions, maxInstructions, maxInstructionLength,
        foundCodeHints, instanceType, parameterTypes, resultType, useRandomGuidance, stage1MaxCounterexamples,
        stage1MaxInputs, stage2MaxCounterexamples, stage2MaxInputs);
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