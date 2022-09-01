package types;

public class MalSymbol implements MalType {
    String sym;

    public MalSymbol(String sym) {
        this.sym = sym;
    }

    public String toString() {
        return sym;
    }
}
