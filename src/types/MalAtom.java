package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;

public class MalAtom extends MalType {

    private MalType value;

    public MalAtom(MalType value) {
        this.value = value;
    }

    @Override
    public MalType evalType(MalEnvironment environment) throws MalExecutionException, TCO {
        return this;
    }

    public MalType value() {
        return value;
    }

    public void setValue(MalType value) {
        this.value = value;
    }

    public String toString() {
        return "(atom "+value.toString()+")";
    }
}
