package uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods;

import java.util.List;

/**
 * 
 */
public class MethodIdentifier {

  /**
   * 
   */
  public final String FullyQualifiedClassName;

  /**
   * 
   */
  public final String MethodName;

  /**
   * 
   */
  public final List<String> FullyQualifiedParameterTypeNames;

  /**
   * @param fullyQualifiedClassName
   * @param methodName
   * @param fullyQualifiedParameterClassNames
   */
  public MethodIdentifier(final String fullyQualifiedClassName, final String methodName,
      final List<String> fullyQualifiedParameterClassNames) {
    FullyQualifiedClassName = fullyQualifiedClassName;
    MethodName = methodName;
    FullyQualifiedParameterTypeNames = fullyQualifiedParameterClassNames;
  }

}
