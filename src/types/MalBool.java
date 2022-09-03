package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Keywords;

public class MalBool implements MalType {

    private boolean value;

    public MalBool(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    @Override
    public MalType eval(MalEnvironment e) {
        return this;
    }

    public String toString() {
        if (value)
            return Keywords.TRUE;
        return Keywords.FALSE;
    }
}
