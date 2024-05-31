package uk.ac.ox.cs.refactoring.synthesis.neural;

public class TextTagger {
    public static String tag(String tagName, String content) {
        return startTag(tagName) + "\n" + content + "\n" + endTag(tagName) + "\n";
    }

    public static String tag(String tagName, String content, Boolean inline) {
      if (inline) {
        return startTag(tagName) + content + endTag(tagName);
      }
      return startTag(tagName) + "\n" + content + "\n" + endTag(tagName) + "\n";
  }

    private static String startTag(String tagName) {
        return "<" + tagName + ">";
    }

    private static String endTag(String tagName) {
        return "</" + tagName + ">";
    }
}
