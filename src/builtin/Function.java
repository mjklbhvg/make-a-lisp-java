package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Evaluator;
import types.*;

import java.util.ArrayList;

public class Function {
    public static MalSpecial lambda() {
        return new MalSpecial() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env, Evaluator evaluator) throws MalExecutionException {
                // TODO: variadic?
                if (args.size() != 3)
                    throw new MalExecutionException("a lambda expects 2 arguments");

                // TODO: maybe accept vectors as well or even hash maps for default parameters
                if (!(args.get(1) instanceof MalList symbolList))
                    throw new MalExecutionException("a lambda needs a list of arguments");

                ArrayList<String> arguments = new ArrayList<>();
                for (MalType t : symbolList) {
                    if (!(t instanceof MalSymbol symbol))
                        throw new MalExecutionException("expected variable name, not " + t);
                    arguments.add(symbol.value());
                }
                return new MalLambda(arguments, args.get(2), env);
            }
        };
    }
}
