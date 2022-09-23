package types;

import environment.MalEnvironment;
import mal.Keywords;

public class MalBool extends MalType {

    private boolean value;

    public MalBool(boolean value) {
        this.value = value;
    }

    @Override
    public MalType evalType(MalEnvironment environment) {
        return this;
    }

    public Object value() {return value;}

    public boolean equals(Object o) {
        if (o instanceof MalBool malbool)
            return malbool.value == value;
        return false;
    }

    public String toString() {
        if (value)
            return Keywords.TRUE;
        return Keywords.FALSE;
    }
}
