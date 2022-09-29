package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class TryCatch {

    public static MalSpecial tryCatch = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.size() < 3) {
                    caller.evalNext(args.get(1), environment);
                    return null;
                }

                MalList c = (MalList) args.get(2);
                if (c.size() != 3
                        || !(c.get(0) instanceof MalSymbol sym)
                        || !sym.value().equals("catch*")
                        || !(c.get(1) instanceof MalSymbol eSym))
                    throw new MalException(new MalString("invalid catch syntax. Use (catch* A B)"));
                try {
                    return args.get(1).eval(environment);
                } catch (MalException e) {
                    MalEnvironment catchEnvironment = new MalEnvironment(environment);
                    catchEnvironment.set(eSym.value(), e.getValue());
                    caller.evalNext(c.get(2), catchEnvironment);
                    return null;
                }
            }
        };

    public static MalCallable invalidCatch = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                throw new MalException(new MalString("catch* is only allowed within a try* form"));
            }
        };

    public static MalCallable throwType = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                throw new MalException(args.get(1));
            }
        };

}
