package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;
import types.*;

public class Conditional {
    public static MalSpecial malIF() {
        return new MalSpecial(new Class[]{MalType.class, MalType.class}, new Class[]{MalType.class}, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException, TCO {
                MalType result = args.get(1).eval(environment);
                boolean conditionTrue = !(result instanceof MalNil);
                if (conditionTrue && result instanceof MalBool resBool)
                    conditionTrue = resBool.value();

                MalType chosen = new MalNil();

                if (conditionTrue)
                    chosen = args.get(2);
                else if (args.size() == 4)
                    chosen = args.get(3);
           //     System.out.println("if chosen: "+chosen);
                throw new TCO(chosen, environment);
            }
        };
    }

    public static MalSpecial malDO() {
        return new MalSpecial(new Class[]{MalType.class}, null, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException, TCO {
                for (int i = 1; i < args.size() - 1; i++)
                    args.get(i).eval(environment);
                throw new TCO(args.get(args.size() - 1), environment);
            }
        };
    }

    public static MalCallable equals() {
        return new MalCallable(new Class[]{MalType.class, MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                if (args.get(1).equals(args.get(2)))
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable greater() {
        return new MalCallable(new Class[]{MalNumber.class, MalNumber.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(((MalNumber) args.get(1)).value() > ((MalNumber) args.get(2)).value());
            }
        };
    }

    public static MalCallable greaterEq() {
        return new MalCallable(new Class[]{MalNumber.class, MalNumber.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(((MalNumber) args.get(1)).value() >= ((MalNumber) args.get(2)).value());
            }
        };
    }

    public static MalCallable less() {
        return new MalCallable(new Class[]{MalNumber.class, MalNumber.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(((MalNumber) args.get(1)).value() < ((MalNumber) args.get(2)).value());
            }
        };
    }

    public static MalCallable lessEq() {
        return new MalCallable(new Class[]{MalNumber.class, MalNumber.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(((MalNumber) args.get(1)).value() <= ((MalNumber) args.get(2)).value());
            }
        };
    }
}
