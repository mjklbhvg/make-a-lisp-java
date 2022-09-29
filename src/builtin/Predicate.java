package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Predicate {
    public static MalCallable isAtom = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalAtom);
            }
        };

    public static MalCallable isList = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalList);
            }
        };

    public static MalCallable isEmpty = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil)
                    return MalBool.TRUE;
                return new MalBool(((MalVector) args.get(1)).isEmpty());
            }
        };

    public static MalCallable isNil = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalNil);
            }
        };

    public static MalCallable isTrue = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalBool bool && bool.value());
            }
        };

    public static MalCallable isFalse = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalBool bool && !(boolean)bool.value());
            }
        };

    public static MalCallable isSymbol = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalSymbol);
            }
        };

    public static MalCallable isKeyword = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalKeyword);
            }
        };

    public static MalCallable isMap = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalTable);
            }
        };

    public static MalCallable isSequential = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalVector);
            }
        };

    public static MalCallable isVector = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalVector && !(args.get(1) instanceof MalList));
            }
        };

    public static MalCallable isString = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalString && !(args.get(1) instanceof MalKeyword));
            }
        };

    public static MalCallable isNumber = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalNumber);
            }
        };

    public static MalCallable isFunction = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalCallable);
            }
        };

    public static MalCallable isMacro = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalMacro);
            }
        };

    public static MalCallable contains = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(((MalTable) args.get(1)).containsKey((MalString) args.get(2)));
            }
        };
}
