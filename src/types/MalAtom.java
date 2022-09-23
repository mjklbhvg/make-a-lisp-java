package types;

import environment.MalEnvironment;

public class MalAtom extends MalType {

    private MalType value;

    public MalAtom(MalType value) {
        this.value = value;
    }

    @Override
    public MalType evalType(MalEnvironment environment) {
        return this;
    }

    public MalType deref() {
        return value;
    }

    public void setReference(MalType value) {
        this.value = value;
    }

    public String toString() {
        return "(atom "+value.toString()+")";
    }
}
