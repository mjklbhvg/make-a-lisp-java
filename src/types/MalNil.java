package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Keywords;

public class MalNil implements MalType {

    @Override
    public MalType eval(MalEnvironment e) {
        return this;
    }

    public String toString() {
        return Keywords.NIL;
    }
}
