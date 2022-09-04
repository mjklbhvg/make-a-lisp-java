package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.*;

public class Conditional {
    public static MalSpecial malIF() {
        return new MalSpecial() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (args.size() != 3 && args.size() != 4)
                    throw new MalExecutionException("if needs 2 or 3 arguments");
                MalType result = args.get(1).eval(env);
                boolean conditionTrue = !(result instanceof MalNil);
                if (conditionTrue && result instanceof MalBool resBool)
                    conditionTrue = resBool.value();

                if (conditionTrue)
                    return args.get(2).eval(env);

                if (args.size() == 4)
                    return args.get(3).eval(env);
                return new MalNil();
            }
        };
    }

    public static MalSpecial malDO() {
        return new MalSpecial() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (args.size() < 2)
                    throw new MalExecutionException("do needs at least 1 argument");
                for (int i = 1; i < args.size() - 1; i++)
                    args.get(i).eval(env);
                return args.get(args.size() - 1).eval(env);
            }
        };
    }

    public static MalCallable equals() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) {
                if (args.size() != 3)
                    return new MalBool(false);
                if (args.get(1).equals(args.get(2)))
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable greater() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (args.size() != 3
                        || !(args.get(1) instanceof MalNumber a)
                        || !(args.get(2) instanceof MalNumber b))
                    throw new MalExecutionException("comparison only works on 2 numbers");

                return new MalBool(a.value() > b.value());
            }
        };
    }

    public static MalCallable greaterEq() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (args.size() != 3
                        || !(args.get(1) instanceof MalNumber a)
                        || !(args.get(2) instanceof MalNumber b))
                    throw new MalExecutionException("comparison only works on 2 numbers");

                return new MalBool(a.value() >= b.value());
            }
        };
    }

    public static MalCallable less() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (args.size() != 3
                        || !(args.get(1) instanceof MalNumber a)
                        || !(args.get(2) instanceof MalNumber b))
                    throw new MalExecutionException("comparison only works on 2 numbers");

                return new MalBool(a.value() < b.value());
            }
        };
    }

    public static MalCallable lessEq() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment env) throws MalExecutionException {
                if (args.size() != 3
                        || !(args.get(1) instanceof MalNumber a)
                        || !(args.get(2) instanceof MalNumber b))
                    throw new MalExecutionException("comparison only works on 2 numbers");

                return new MalBool(a.value() <= b.value());
            }
        };
    }

}