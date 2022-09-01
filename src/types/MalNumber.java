package types;

public class MalNumber implements MalType {

    private double value;

    public MalNumber(double value) {
        this.value = value;
    }

    public String toString() {
        return Double.toString(value);
    }

}
