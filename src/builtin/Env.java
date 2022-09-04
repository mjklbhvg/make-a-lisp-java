package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.*;

public class Env {
    public static MalSpecial addEnvironment() {
        return new MalSpecial() {
            public MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (!(args.size() == 3))
                    throw new MalExecutionException("let* takes 2 arguments");

                if (!(args.get(1) instanceof MalList bindings))
                    throw new MalExecutionException("the first argument to let* must be a list");
                if (bindings.size() % 2 != 0)
                    throw new MalExecutionException("mismatched keys and values in let*");

                MalEnvironment newEnv = new MalEnvironment(env);
                for (int i = 0; i < bindings.size(); i += 2) {
                    if (!(bindings.get(i) instanceof MalSymbol sym))
                        throw new MalExecutionException("expected a variable name, not" + bindings.get(i));
                    newEnv.set(sym.value(), bindings.get(i + 1));
                }
                return args.get(2).eval(newEnv);
            }
        };
    }

    public static MalSpecial modifyEnvironment() {
        return new MalSpecial() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (!(args.size() == 3))
                    throw new MalExecutionException("def! takes 2 arguments");

                if (!(args.get(1) instanceof MalSymbol sym))
                    throw new MalExecutionException("expected a variable name, not" + args.get(1));

                MalType value = args.get(2).eval(env);
                env.set(sym.value(), value);
                return value;
            }
        };
    }
}
