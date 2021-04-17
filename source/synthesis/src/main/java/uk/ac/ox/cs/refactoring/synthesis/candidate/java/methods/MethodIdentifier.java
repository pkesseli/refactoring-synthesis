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
   * 
   */
  public final String FullyQualifiedReturnTypeName;

  /**
   * 
   */
  public final boolean IsStatic;

  /**
   * @param fullyQualifiedClassName
   * @param methodName
   * @param fullyQualifiedParameterClassNames
   * @param fullyQualifiedReturnTypeName
   * @param isStatic
   */
  public MethodIdentifier(final String fullyQualifiedClassName, final String methodName,
      final List<String> fullyQualifiedParameterClassNames, final String fullyQualifiedReturnTypeName,
      final boolean isStatic) {
    FullyQualifiedClassName = fullyQualifiedClassName;
    MethodName = methodName;
    FullyQualifiedParameterTypeNames = fullyQualifiedParameterClassNames;
    FullyQualifiedReturnTypeName = fullyQualifiedReturnTypeName;
    IsStatic = isStatic;
  }

}
