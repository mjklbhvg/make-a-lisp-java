package types;

import exceptions.MalExecutionException;
import mal.Evaluator;

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
    public MalType eval(Evaluator evaluator) throws MalExecutionException {
            return evaluator.getEnvironment().get(sym)
                    .eval(evaluator);
    }

    @Override
    public String rawString() {
        return toString();
    }
}
