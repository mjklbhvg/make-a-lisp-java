package types;

import environment.MalEnvironment;
import exceptions.MalException;

public class MalMacro extends MalType {

    private MalLambda lambda;

    public MalMacro(MalLambda lambda) {
        this.lambda = lambda;
    }

    public MalType expand(MalList args, MalEnvironment environment) throws MalException {

        MalType executor = new MalType() {
            @Override
            public MalType evalType(MalEnvironment environment, MalType caller) throws MalException {
                return lambda.execute(args, null, this);
            }
        };
        MalType result = executor.eval(null);
        MalMacro nextMacro;
        while ((nextMacro = isMacroCall(result, environment)) != null) {
            result = nextMacro.expand((MalList) result, environment);
        }
       return result;
    }

    public static MalMacro isMacroCall(MalType t, MalEnvironment env) {
        if (!(t instanceof MalList list))
            return null;
        if (list.isEmpty())
            return null;

        try {
            if (!(list.get(0) instanceof MalSymbol sym))
                return null;
            if (sym.eval(env) instanceof MalMacro macro)
                return macro;
            return null;
        } catch (MalException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "#<macro>";
    }

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }
}
