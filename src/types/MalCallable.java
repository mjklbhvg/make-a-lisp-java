package types;

import exceptions.MalExecutionException;
import mal.Evaluator;

public abstract class MalCallable implements MalType {
    protected abstract MalType execute(MalList args, Evaluator evaluator) throws MalExecutionException;

    @Override
    public MalType eval(Evaluator evaluator) {
        return this;
    }

    public String toString() {
        return "#<function>";
    }

    public String rawString() {
        return toString();
    }
}
