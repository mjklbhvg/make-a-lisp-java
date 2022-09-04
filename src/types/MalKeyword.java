package types;

import environment.MalEnvironment;

public class MalKeyword implements MalType {

    private String word;

    public MalKeyword(String word) {
        this.word = word;
    }

    public String value() {
        return word;
    }

    @Override
    public MalType eval(MalEnvironment e) {
        return this;
    }

    @Override
    public String rawString() {
        return toString();
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
