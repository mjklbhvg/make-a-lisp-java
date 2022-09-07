package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public class MalNumber extends MalType {

    private double value;

    public MalNumber(double value) {
        this.value = value;
    }

    public double value() {return  value;}

    public boolean equals(Object o) {
        if (o instanceof MalNumber num)
            return value == num.value;
        return false;
    }

    public String toString() {
        String str = Double.toString(value);
        if (str.endsWith(".0")) // FIXME: LOL
            return str.substring(0, str.length() - 2);
        return str;
    }

    public static void assertIsNumber(MalType t) throws MalExecutionException {
        if (!(t instanceof MalNumber))
            throw new MalExecutionException("'" + t.toString() + "' can't be used as a number");
    }

    @Override
    public MalType evalType(MalEnvironment environment) {
        return this;
    }
}
