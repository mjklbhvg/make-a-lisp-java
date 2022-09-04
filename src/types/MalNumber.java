package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Evaluator;

public class MalNumber implements MalType {

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
        return Double.toString(value);
    }

    public static void assertIsNumber(MalType t) throws MalExecutionException {
        if (!(t instanceof MalNumber))
            throw new MalExecutionException("'" + t.toString() + "' can't be used as a number");
    }

    @Override
    public MalType eval(MalEnvironment e, Evaluator evaluator) {
        return this;
    }

    @Override
    public String rawString() {
        return toString();
    }
}
