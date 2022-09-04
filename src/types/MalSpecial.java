package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public interface MalSpecial extends MalCallable {
    MalType execute(MalList args, MalEnvironment env) throws MalExecutionException;

    @Override
    default MalType execute(MalList args) throws MalExecutionException {
        return execute(args, MalEnvironment.getBaseEnvironment());
    }

    @Override
    default MalType eval(MalEnvironment e) {
        return this;
    }
}
