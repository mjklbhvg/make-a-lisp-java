package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public abstract class MalCallable implements MalType {
    protected abstract MalType execute(MalList args, MalEnvironment env) throws MalExecutionException;

    @Override
    public MalType eval(MalEnvironment env) {
        return this;
    }

    public String toString() {
        return "#<function>";
    }

    public String rawString() {
        return toString();
    }
}
