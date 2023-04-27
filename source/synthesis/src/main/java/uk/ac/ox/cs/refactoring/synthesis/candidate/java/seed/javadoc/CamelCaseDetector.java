package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper to extract sequences which look like method names or method
 * invocations sample code from plain texte. This is a last resort code hint
 * extraction mechanism and is implemented very conservatively, to avoid trying
 * to interpret unrelated words as potential refactoring candidates.
 */
class CamelCaseDetector {

  /**
   * Conservative camel case regex intended to recognise names like
   * {@code sleepFor10Minutes}, but not {@code sleep}, since the latter might lead
   * to too many false positives. Also ignores the issue of Java key words or
   * non-latin characters.
   */
  private static final String CAMEL_CASE_PATTERN = "[a-z][a-zA-Z0-9]*[A-Z][a-zA-Z0-9]*";

  /**
   * Pattern for Java method invocation code example used e.g. in
   * {@link Character#isJavaLetter(char)}. This is again a very conservative
   * pattern that would not recognise the code example if there were e.g. spaces
   * between the arguments.
   */
  private static final Pattern METHOD_INVOCATION = Pattern.compile(CAMEL_CASE_PATTERN + "\\([^)]*\\)");

  /**
   * Matches anything camel case. Intended to parse things like
   * {@link java.net.DatagramSocketImpl#getTTL()}'s deprecated JavaDoc section.
   */
  private static final Pattern IDENTIFIER = Pattern.compile(CAMEL_CASE_PATTERN);

  /**
   * Finds the first substring in {@code deprecatedJavaDoc} which matches
   * {@link #METHOD_INVOCATION}.
   * 
   * @param deprecatedJavaDoc JavaDoc in a {@code @deprecated} section.
   * @return First substring matching our pattern of a plain text method
   *         invocation or {@code null} if none found.
   */
  static String getMethodInvocation(final String deprecatedJavaDoc) {
    final Matcher matcher = METHOD_INVOCATION.matcher(deprecatedJavaDoc);
    return matcher.find() ? matcher.group() : null;
  }

  /**
   * Finds the first substring in {@code deprecatedJavaDoc} which matches
   * {@link #IDENTIFIER}.
   * 
   * @param deprecatedJavaDoc JavaDoc in a {@code @deprecated} section.
   * @return First substring matching our restricted pattern of a camel case Java
   *         identivier or {@code null} if none found.
   */
  static String getMethodName(final String deprecatedJavaDoc) {
    final Matcher matcher = IDENTIFIER.matcher(deprecatedJavaDoc);
    return matcher.find() ? matcher.group() : null;
  }
}
