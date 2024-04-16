package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CodeEngine extends QueryEngine {
    static final String CONSTRAINT_NOEXPLAIN = "Show me code only, no explanation";
    static final String CONSTRAINT_FORMAT = "Place your code inside a <code></code> tag";

    public default String generateCode(Prompt prompt) throws NoSuchElementException {
      prompt.constraints.add(CONSTRAINT_NOEXPLAIN);
      prompt.constraints.add(CONSTRAINT_FORMAT);
      try {
        String response = extract(query(prompt));
        return response;
      } catch (final Exception e) {
        e.printStackTrace();
        throw new NoSuchElementException(e.getMessage());
      }
    }


    public static String extract(String response) {
        Pattern tagged = Pattern.compile("<code>(?<code>[\\s\\S]*)</code>");
        Matcher taggedBlock = tagged.matcher(response);
        if (taggedBlock.find()) {
            return taggedBlock.group("code");
        }
        Pattern backticked = Pattern.compile("```(java)?(?<code>[\\s\\S]*)```");
        Matcher backtickedBlock = backticked.matcher(response);
        if (backtickedBlock.find()) {
            return backtickedBlock.group("code");
        }
        return response;
    }
}
