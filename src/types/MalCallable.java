package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Evaluator;

public abstract class MalCallable implements MalType {
    protected abstract MalType execute(MalList args, MalEnvironment env, Evaluator evaluator) throws MalExecutionException;

    @Override
    public MalType eval(MalEnvironment env, Evaluator evaluator) {
        return this;
    }

    public String toString() {
        return "#<function>";
    }

    public String rawString() {
        return toString();
    }
}
