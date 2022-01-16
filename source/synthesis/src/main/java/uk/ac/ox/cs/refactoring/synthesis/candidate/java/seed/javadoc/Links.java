package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.javadoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Helper to parse Javadoc links. */
class Links {

  /** Groups Javadoc links by method identifier and link text. */
  private static final Pattern PATTERN = Pattern.compile("\\s*([^ ]+(\\([^)]+\\)))?(\\s+.*)?");

  /**
   * Retrieves the method link out of a Javadoc link content, which may consist of
   * just a method link or a method link followed by a link text.
   * 
   * @param content Javadoc link content.
   * @return Method link text without link text, if present.
   */
  static String getLink(final String content) {
    final Matcher matcher = PATTERN.matcher(content);
    if (!matcher.matches())
      return content;

    final String linkOnly = matcher.group(1);
    return linkOnly != null ? linkOnly : content;
  }

  private Links() {
  }
}
