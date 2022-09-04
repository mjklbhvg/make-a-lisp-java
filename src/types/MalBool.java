package types;

import environment.MalEnvironment;
import mal.Keywords;

public class MalBool implements MalType {

    private boolean value;

    public MalBool(boolean value) {
        this.value = value;
    }

    @Override
    public MalType eval(MalEnvironment e) {
        return this;
    }

    public boolean value() {return value;}

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

    public String rawString() {
        return toString();
    }
}
