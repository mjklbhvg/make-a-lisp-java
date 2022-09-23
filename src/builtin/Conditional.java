package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import mal.TCO;
import types.*;

public class Conditional {
    public static MalSpecial malIF() {
        return new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws TCO, MalException {
                MalType result = args.get(1).eval(environment);
                boolean conditionTrue = !(result instanceof MalNil);
                if (conditionTrue && result instanceof MalBool resBool)
                    conditionTrue = (boolean) resBool.value();

                MalType chosen = new MalNil();

                if (conditionTrue)
                    chosen = args.get(2);
                else if (args.size() == 4)
                    chosen = args.get(3);
                throw new TCO(chosen, environment);
            }
        };
    }

    public static MalSpecial malDO() {
        return new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws TCO, MalException {
                for (int i = 1; i < args.size() - 1; i++)
                    args.get(i).eval(environment);
                throw new TCO(args.get(args.size() - 1), environment);
            }
        };
    }

    public static MalCallable equals() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                if (args.get(1).equals(args.get(2)))
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable greater() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                return new MalBool((double)(args.get(1)).value() > (double) (args.get(2)).value());
            }
        };
    }

    public static MalCallable greaterEq() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                return new MalBool((double) (args.get(1)).value() >= (double) (args.get(2)).value());
            }
        };
    }

    public static MalCallable less() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                return new MalBool((double) (args.get(1)).value() < (double) (args.get(2)).value());
            }
        };
    }

    public static MalCallable lessEq() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                return new MalBool((double) (args.get(1)).value() <= (double) (args.get(2)).value());
            }
        };
    }
}
