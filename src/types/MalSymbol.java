package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public class MalSymbol implements MalType {
     private String sym;

    public MalSymbol(String sym) {
        this.sym = sym;
    }

    public String toString() {
        return sym;
    }

    public String value() {return sym;}

    @Override
    public MalType eval(MalEnvironment e) throws MalExecutionException {
            return e.get(sym).eval(e);
    }

    @Override
    public String rawString() {
        return toString();
    }
}
