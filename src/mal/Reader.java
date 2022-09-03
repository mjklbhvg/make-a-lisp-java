package mal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {

    private static final String MAL_TOKEN_REGEX =
            "([\\s,]*)(~@|[\\[\\]{}()'`~^@]|\"(?:\\\\.|[^\\\\\"])*\"?|;.*|[^\\s\\[\\]{}('\"`,;)]*)";

    private static final Pattern MAL_TOKEN_PATTERN = Pattern.compile(MAL_TOKEN_REGEX);
    private List<String> tokens;
    private int cursor;

    public Reader(String input) {
        cursor = 0;
        Matcher m = MAL_TOKEN_PATTERN.matcher(input.strip());
        tokens = new ArrayList<String>();
        while (m.find())
            tokens.add(m.group(2));

        // remove empty tokens
        tokens.remove("");

        // remove comments
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).startsWith(";")) {
                tokens.remove(i);
                i--;
            }
        }
    }

    // TODO: read from files
    // public mal.Reader (File f) {}

    public String next() {
        if (!hasNext())
            return null;
        cursor++;
        return tokens.get(cursor - 1);
    }

    public String peek() {
        return tokens.get(cursor);
    }

    public boolean peekAt(String expected) {
        if (expected.equals(peek())) {
            next();
            return true;
        }
        return false;
    }

    public boolean hasNext() {
        return (cursor < tokens.size());
    }

    public List<String> getTokens() {
        return tokens;
    }
}
