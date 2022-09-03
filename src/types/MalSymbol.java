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

    @Override
    public MalType eval(MalEnvironment e) throws MalExecutionException {
        if (e.containsKey(sym)) {
            return e.get(sym);
        }
        throw new MalExecutionException("variable '" + sym + "' not defined");
    }
}
