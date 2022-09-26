package types;

import environment.MalEnvironment;
import exceptions.MalException;

public abstract class MalCallable extends MalType {
    public abstract MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException;

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }

    public String toString() {
        return "#<function>";
    }
}
