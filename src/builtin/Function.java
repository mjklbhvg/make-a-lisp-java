package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;
import types.*;

import java.util.ArrayList;

public class Function {
    public static MalSpecial lambda() {
        return new MalSpecial() {
            @Override
            protected MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 3)
                    throw new MalExecutionException("a lambda expects 2 arguments");

                // TODO: maybe accept vectors as well or even hash maps for default parameters
                if (!(args.get(1) instanceof MalList symbolList))
                    throw new MalExecutionException("a lambda needs a list of arguments");

                ArrayList<String> arguments = new ArrayList<>();
                for (int i = 0; i < symbolList.size(); i++) {
                    if (!(symbolList.get(i) instanceof MalSymbol symbol))
                        throw new MalExecutionException("expected variable name, not " + symbolList.get(i));
                    arguments.add(symbol.value());
                }
                return new MalLambda(arguments, args.get(2), environment);
            }
        };
    }

    public static MalCallable eval() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException, TCO {
                if (args.size() != 2)
                    throw new MalExecutionException("eval expects 1 argument");
                throw new TCO(args.get(1), environment);
            }
        };
    }

}
