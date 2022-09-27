package types;

import environment.MalEnvironment;

public class MalNumber extends MalType {

    private long value;

    public MalNumber(long value) {
        this.value = value;
    }

    public long value() {return  value;}

    public boolean equals(Object o) {
        if (o instanceof MalNumber num)
            return value == num.value;
        return false;
    }

    public String toString() {
        return Long.toString(value);
    }

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }
}
