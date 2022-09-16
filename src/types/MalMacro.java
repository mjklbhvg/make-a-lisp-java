package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;

public class MalMacro extends MalType {

    private MalLambda lambda;

    public MalMacro(MalLambda lambda) {
        this.lambda = lambda;
    }

    public MalList expand(MalList args, MalEnvironment environment) throws MalExecutionException, TCO {
        MalType result = null;
        try {
            lambda.executeChecked(args, environment);
        } catch (TCO tco) {
            result = tco.evalNext.eval(tco.nextEnvironment);
        }
        while (isMacroCall(result, environment)) {
            result = result.eval(environment);
        }
        if (!(result instanceof MalList list))
            throw new TCO(result, environment);
        return list;
    }

    public static boolean isMacroCall(MalType t, MalEnvironment env) {
        if (!(t instanceof MalList list))
            return false;
        if (list.isEmpty())
            return false;
        if (!(list.get(0) instanceof MalSymbol sym))
            return false;
        try {
            return (sym.eval(env) instanceof MalMacro);
        } catch (MalExecutionException e) {
            return false;
        }

    }

    @Override
    public String toString() {
        return "#<macro>";
    }

    @Override
    public MalType evalType(MalEnvironment environment) throws MalExecutionException, TCO {
        return this;
    }
}
