package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeEngine extends QueryEngine {
    private static final String CONSTRAINT_NOEXPLAIN = "Show me code only, no explanation";
    private static final String CONSTRAINT_FORMAT = "Place your code inside a <code></code> tag";


    public String generateCode(String prompt) {
        String constrained = prompt + "\nTake the following constraints into consideration:\n"
            + "<list>\n"
            + "\t1. " + CONSTRAINT_NOEXPLAIN + "\n"
            + "\t2. " + CONSTRAINT_FORMAT + "\n"
            + "</list>\n";


        String response = query(constrained);
        return extract(response);
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
