package mal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public Reader (Path path) throws IOException {
        this(Files.readString(path));
    }

    public Reader(String input) {
        cursor = 0;
        Matcher m = MAL_TOKEN_PATTERN.matcher(input.strip());
        tokens = new ArrayList<>();
        while (m.find()) {
            // remove comments and empty tokens
            if (!m.group(2).startsWith(";")
                    && !m.group(2).isEmpty())
                tokens.add(m.group(2));
        }
    }

    public String next() {
        if (!hasNext())
            return null;
        return tokens.get(cursor++);
    }

    public String peek() {
        if (!hasNext())
            return null;
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
