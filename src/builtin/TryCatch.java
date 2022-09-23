package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import mal.TCO;
import types.*;

public class TryCatch {

    public static MalSpecial tryCatch() {
        return new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException, TCO {
                if (args.size() < 3)
                    throw new TCO(args.get(1), environment);

                MalList c = (MalList) args.get(2);
                if (c.size() != 3
                        || !(c.get(0) instanceof MalSymbol sym)
                        || !sym.value().equals("catch*")
                        || !(c.get(1) instanceof MalSymbol eSym))
                    throw new MalException(new MalString("invalid catch syntax. Use (catch* A B)"));

                try {
                    return args.get(1).eval(environment);
                } catch (MalException e) {
                    MalEnvironment newEnv = new MalEnvironment(environment);
                    newEnv.set(eSym.value(), e.getValue());
                    throw new TCO(c.get(2), newEnv);
                }
            }
        };
    }

    public static MalCallable invalidCatch() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                throw new MalException(new MalString("catch* is only allowed within a try* form"));
            }
        };
    }

    public static MalCallable throwType() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                throw new MalException(args.get(1));
            }
        };
    }

}
