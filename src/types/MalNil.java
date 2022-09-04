package types;

import environment.MalEnvironment;
import mal.Evaluator;
import mal.Keywords;

public class MalNil implements MalType {

    @Override
    public MalType eval(MalEnvironment e, Evaluator evaluator) {
        return this;
    }

    @Override
    public String rawString() {
        return toString();
    }

    public String toString() {
        return Keywords.NIL;
    }

    public boolean equals(Object o) {
        return o instanceof MalNil;
    }
}
