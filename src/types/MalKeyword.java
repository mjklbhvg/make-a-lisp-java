package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

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

    public String toString() {
        return ":" + word;
    }
}
