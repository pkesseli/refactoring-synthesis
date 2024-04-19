package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CodeEngine implements QueryEngine {
    private static final String CONSTRAINT_NOEXPLAIN = "Show me code only, no explanation";
    private static final String CONSTRAINT_FORMAT = "Place your code inside a <code></code> tag";
    private static final Logger LOGGER = Logger.getLogger(CodeEngine.class.getName());

    public final String generateCode(Prompt prompt) throws NoSuchElementException {
      prompt.constraints.add(CONSTRAINT_NOEXPLAIN);
      prompt.constraints.add(CONSTRAINT_FORMAT);
      try {
        String response = extract(query(prompt));
        LOGGER.log(Level.INFO, "\n\nUSER:\n{0}\n\nASSISTANT:\n{1}", new Object[]{prompt.toString(), response});
        return response;
      } catch (final Exception e) {
        e.printStackTrace();
        throw new NoSuchElementException(e.getMessage());
      }
    }


    public static String extract(String response) {
      // Match as less as possible
      Pattern tagged = Pattern.compile("<code>(?<code>[\\s\\S]*?)</code>");
      Matcher taggedBlock = tagged.matcher(response);
      List<String> allMatches = new ArrayList<String>();
      while (taggedBlock.find()) {
        allMatches.add(taggedBlock.group("code"));
      }
      Pattern backticked = Pattern.compile("```(java)?(?<code>[\\s\\S]*?)```");
      Matcher backtickedBlock = backticked.matcher(response);
      while (backtickedBlock.find()) {
        allMatches.add(backtickedBlock.group("code"));
      }

      if (allMatches.size() > 0) {
        return allMatches.get(0);
      }
      throw new NoSuchElementException();
    }
}
