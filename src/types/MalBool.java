package types;

import environment.MalEnvironment;

public class MalBool extends MalType {

    public static MalBool TRUE = new MalBool(true),
                          FALSE = new MalBool(false);
    private boolean value;

    public MalBool(boolean value) {
        this.value = value;
    }

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
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
            return "true";
        return "false";
    }
}
