package types;

import environment.MalEnvironment;
import exceptions.MalParserException;

public class MalString extends MalType {

    private String value;

    public MalString(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }

    public boolean equals(Object o) {
        if (o instanceof MalString str)
            return str.value.equals(value);
        return false;
    }

    public static String escape(String s, boolean visualizeEscape) {
        String pre = "", post = "";
        if (visualizeEscape) {
            pre = "\033[31m";
            post = "\033[m";
        }
        return s.replace("\\", pre + "\\\\" + post)
                .replace("\"", pre + "\\\"" + post)
                .replace("\n", pre + "\\n" + post);
    }

    public static String unescape(String s) throws MalParserException {
        if (s.length() < 2)
            throw new MalParserException("unbalanced string");
        StringBuilder str = new StringBuilder();
        boolean escapeActive = false;
        char[] chars = s.toCharArray();
        for (int i = 1; i < chars.length - 1; i++) {
            if (escapeActive) {
                escapeActive = false;
                switch (chars[i]) {
                    case '\\' -> str.append('\\');
                    case 'n' -> str.append('\n');
                    case '"' -> str.append('"');
                    default -> throw new MalParserException("invalid escape character '" + chars[i] + "'");
                }
                continue;
            }

            if (chars[i] == '\\') {
                escapeActive = true;
                continue;
            }
            str.append(chars[i]);
        }
        if (escapeActive)
            throw new MalParserException("attempt to escape string end");
        return str.toString();
    }

    public String prettyPrint() {
        return "„" + escape(value, true) + "“";
    }
    public String toString() {return value;}
}
