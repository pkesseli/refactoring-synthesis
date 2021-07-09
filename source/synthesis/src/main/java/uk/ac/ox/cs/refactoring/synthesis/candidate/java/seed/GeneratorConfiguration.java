package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.util.List;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;

/**
 * Configures candidate generators.
 */
public class GeneratorConfiguration {

  /**
   * Instruction set to use.
   */
  public final ComponentDirectory Components;

  /**
   * Minimum number of instructions.
   */
  public final byte MinInstructions;

  /**
   * Maximum number of instructions.
   */
  public final byte MaxInstructions;

  /**
   * Maximum instruction length.
   */
  public final byte MaxInstructionLength;

  /**
   * Instance type of counterexample root.
   */
  public final Class<?> InstanceType;

  /**
   * Parameter types of counterexample root.
   */
  public final List<Class<?>> ParameterTypes;

  /**
   * Program result type.
   */
  public final Class<?> ResultType;

  /**
   * @param components           {@link #Components}
   * @param minInstructions      {@link #MinInstructions}
   * @param maxInstructions      {@link #MaxInstructions}
   * @param maxInstructionLength {@link #MaxInstructionLength}
   * @param instanceType         {@link #InstanceType}
   * @param parameterTypes       {@link #ParameterTypes}
   * @param resultType           {@link #ResultType}
   */
  public GeneratorConfiguration(final ComponentDirectory components, final byte minInstructions,
      final byte maxInstructions, final byte maxInstructionLength, final Class<?> instanceType,
      final List<Class<?>> parameterTypes, final Class<?> resultType) {
    Components = components;
    MinInstructions = minInstructions;
    MaxInstructions = maxInstructions;
    MaxInstructionLength = maxInstructionLength;
    InstanceType = instanceType;
    ParameterTypes = parameterTypes;
    ResultType = resultType;
  }
}
