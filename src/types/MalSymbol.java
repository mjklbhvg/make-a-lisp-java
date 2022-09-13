package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public class MalSymbol extends MalType {
     private String sym;

    public MalSymbol(String sym) {
        this.sym = sym;
    }

    public String toString() {
        return sym;
    }

    public String value() {return sym;}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MalSymbol s))
            return false;
        return sym.equals(s.sym);
    }

    @Override
    public MalType evalType(MalEnvironment environment) throws MalExecutionException {
            return environment.get(sym);
    }
}
