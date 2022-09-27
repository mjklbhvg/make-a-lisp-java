package types;

import environment.MalEnvironment;

public class MalKeyword extends MalString {

    public MalKeyword(String word) {
        super(word);
    }

    public String value() {
        return "\0"+super.value();
    }

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }

    public boolean equals(Object o) {
        if (o instanceof MalKeyword malkeyword)
            return malkeyword.value().equals(value());
        return false;
    }

    public String toString() {
        return ":" + super.value();
    }
    public String prettyPrint(){return toString();}
}
