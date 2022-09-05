package types;

import mal.Evaluator;

public abstract class MalSpecial extends MalCallable {
    @Override
    public MalType eval(Evaluator evaluator) {
        return this;
    }

    public String toString() {
        return "#<special>";
    }

}
