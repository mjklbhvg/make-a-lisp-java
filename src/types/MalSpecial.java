package types;

import environment.MalEnvironment;
import mal.Evaluator;

public abstract class MalSpecial extends MalCallable {
    @Override
    public MalType eval(MalEnvironment e, Evaluator evaluator) {
        return this;
    }

    public String toString() {
        return "#<special>";
    }

}
