package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Conditional {
    public static MalSpecial malIF = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalType result = args.get(1).eval(environment);
                boolean conditionTrue = !(result instanceof MalNil);
                if (conditionTrue && result instanceof MalBool resBool)
                    conditionTrue = resBool.value();

                MalType chosen = MalNil.NIL;

                if (conditionTrue)
                    chosen = args.get(2);
                else if (args.size() == 4)
                    chosen = args.get(3);
                caller.evalNext(chosen, environment);
                return null;
            }
        };

    public static MalSpecial malDO = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                for (int i = 1; i < args.size() - 1; i++)
                    args.get(i).eval(environment);
                caller.evalNext(args.get(args.size() - 1), environment);
                return null;
            }
        };

    public static MalCallable equals = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1).equals(args.get(2)));
            }
        };

    public static MalCallable greater = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(((MalNumber) args.get(1)).value() > ((MalNumber) args.get(2)).value());
            }
        };

    public static MalCallable greaterEq = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(((MalNumber) args.get(1)).value() >= ((MalNumber)args.get(2)).value());
            }
        };

    public static MalCallable less = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(((MalNumber) args.get(1)).value() < ((MalNumber) args.get(2)).value());
            }
        };

    public static MalCallable lessEq = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(((MalNumber) args.get(1)).value() <= ((MalNumber) args.get(2)).value());
            }
        };
}
