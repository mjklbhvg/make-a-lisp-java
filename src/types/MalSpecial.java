package types;

import environment.MalEnvironment;

public abstract class MalSpecial extends MalCallable {
    @Override
    public MalType eval(MalEnvironment e) {
        return this;
    }

    public String toString() {
        return "#<special>";
    }

}
