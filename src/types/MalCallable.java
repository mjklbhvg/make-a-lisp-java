package types;

import environment.MalEnvironment;
import exceptions.MalException;
import mal.TCO;

public abstract class MalCallable extends MalType {
    public abstract MalType execute(MalList args, MalEnvironment environment) throws TCO, MalException;

     public MalCallable() {
     }

    @Override
    public MalType evalType(MalEnvironment environment) {
        return this;
    }

    public String toString() {
        return "#<function>";
    }
}
