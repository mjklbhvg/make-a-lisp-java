package types;

import environment.MalEnvironment;
import mal.Keywords;

public class MalNil extends MalType {

    @Override
    public MalType evalType(MalEnvironment environment) {
        return this;
    }

    public String toString() {
        return Keywords.NIL;
    }

    public boolean equals(Object o) {
        return o instanceof MalNil;
    }

    public Object value() {
        return null;
    }
}
