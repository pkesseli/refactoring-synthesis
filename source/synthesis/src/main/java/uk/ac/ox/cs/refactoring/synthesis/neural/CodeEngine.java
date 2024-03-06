package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeEngine extends QueryEngine {
    private static final String CONSTRAINT_NOEXPLAIN = "Show me code only, no explanation";
    private static final String CONSTRAINT_FORMAT = "Place your code inside a <code></code> tag";

    public String generateCode(Prompt prompt) {
        return generateCodeN(prompt, 1).get(0);
    }

    public List<String> generateCodeN(Prompt prompt, int beamSize) {
      prompt.constraints.add(CONSTRAINT_NOEXPLAIN);
      prompt.constraints.add(CONSTRAINT_FORMAT);
      // TODO log this
      // System.out.println(prompt);
      List<String> allCode = queryN(prompt, beamSize).stream().map(response -> extract(response)).collect(Collectors.toList());
      return allCode;
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
