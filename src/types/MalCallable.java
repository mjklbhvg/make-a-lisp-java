package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public interface MalCallable extends MalType {
    MalType execute(MalList args, MalEnvironment env) throws MalExecutionException;

    @Override
    default MalType eval(MalEnvironment e) {
        return this;
    }
}
