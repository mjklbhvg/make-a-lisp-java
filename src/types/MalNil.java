package types;

import environment.MalEnvironment;

public class MalNil extends MalType {

    public static final MalNil NIL = new MalNil();
    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }

    public String toString() {
        return "nil";
    }

    public boolean equals(Object o) {
        return o instanceof MalNil;
    }

    public MalNil value() {
        return NIL;
    }
}
