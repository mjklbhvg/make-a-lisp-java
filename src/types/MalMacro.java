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
                return lambda.execute(args, environment, this);
            }
        };

        MalType result = executor.eval(environment);
        while (isMacroCall(result, environment)) {
            result = ((MalMacro) ((MalList) result).get(0)).expand((MalList) result, environment);
        }
       return result;
    }

    public static boolean isMacroCall(MalType t, MalEnvironment env) {
        if (!(t instanceof MalList list))
            return false;
        if (list.isEmpty())
            return false;

        try {
            if (!(list.get(0) instanceof MalSymbol sym))
                return false;
            return (sym.eval(env) instanceof MalMacro);
        } catch (MalException e) {
            return false;
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
