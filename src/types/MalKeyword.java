package types;

import environment.MalEnvironment;

public class MalKeyword extends MalType {
    private String word;

    public MalKeyword(String word) {
        this.word = word;
    }

    public String value() {
        return toString();
    }

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }

    public boolean equals(Object o) {
        if (o instanceof MalKeyword malkeyword)
            return malkeyword.value().equals(word);
        return false;
    }

    public String toString() {
        return ":" + word;
    }
}
