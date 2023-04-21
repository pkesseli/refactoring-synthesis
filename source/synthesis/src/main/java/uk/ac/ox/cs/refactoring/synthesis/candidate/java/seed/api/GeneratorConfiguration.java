package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api;

import java.util.List;

import com.fasterxml.classmate.ResolvedType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;

/** Configures candidate generators. */
public class GeneratorConfiguration {

  /** Instruction set to use. */
  public final ComponentDirectory Components;

  /** Minimum number of instructions. */
  public final byte MinInstructions;

  /** Maximum number of instructions. */
  public final byte MaxInstructions;

  /** Maximum instruction length. */
  public final byte MaxInstructionLength;

  /** Indicates whether Javadoc code hints were found */
  public final boolean FoundCodeHints;

  /** Instance type of counterexample root. */
  public final ResolvedType InstanceType;

  /** Parameter types of counterexample root. */
  public final List<ResolvedType> ParameterTypes;

  /** Program result type. */
  public final ResolvedType ResultType;

  /** Use no guidance instead of Zest guidance. */
  public final boolean UseRandomGuidance;

  /** Maximum number of genuine counterexamples in the first stage. */
  public final long Stage1MaxCounterexamples;

  /** Maximum number of inputs in the first stage. */
  public final long Stage1MaxInputs;

  /** Maximum number of genuine counterexamples in the second stage. */
  public final long Stage2MaxCounterexamples;

  /** Maximum number of inputs in the second stage. */
  public final long Stage2MaxInputs;

  /**
   * @param components               {@link #Components}
   * @param minInstructions          {@link #MinInstructions}
   * @param maxInstructions          {@link #MaxInstructions}
   * @param maxInstructionLength     {@link #MaxInstructionLength}
   * @param foundCodeHints           {@link #FoundCodeHints}
   * @param instanceType             {@link #InstanceType}
   * @param parameterTypes           {@link #ParameterTypes}
   * @param resultType               {@link #ResultType}
   * @param useRandomGuidance        {@link #UseRandomGuidance}
   * @param stage1MaxCounterexamples {@link #Stage1MaxCounterexamples}
   * @param stage1MaxInputs          {@link #Stage1MaxInputs}
   * @param stage2MaxCounterexamples {@link #Stage2MaxCounterexamples}
   * @param stage2MaxInputs          {@link #Stage2MaxInputs}
   */
  public GeneratorConfiguration(final ComponentDirectory components, final byte minInstructions,
      final byte maxInstructions, final byte maxInstructionLength, final boolean foundCodeHints,
      final ResolvedType instanceType, final List<ResolvedType> parameterTypes, final ResolvedType resultType,
      final boolean useRandomGuidance, final long stage1MaxCounterexamples, final long stage1MaxInputs,
      final long stage2MaxCounterexamples, final long stage2MaxInputs) {
    Components = components;
    MinInstructions = minInstructions;
    MaxInstructions = maxInstructions;
    MaxInstructionLength = maxInstructionLength;
    FoundCodeHints = foundCodeHints;
    InstanceType = instanceType;
    ParameterTypes = parameterTypes;
    ResultType = resultType;
    UseRandomGuidance = useRandomGuidance;
    Stage1MaxCounterexamples = stage1MaxCounterexamples;
    Stage1MaxInputs = stage1MaxInputs;
    Stage2MaxCounterexamples = stage2MaxCounterexamples;
    Stage2MaxInputs = stage2MaxInputs;
  }
}