package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Predicate {
    public static MalCallable isAtom() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalAtom);
            }
        };
    }

    public static MalCallable isList() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalBool(args.get(1) instanceof MalList);
            }
        };
    }

    public static MalCallable isEmpty() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil)
                    return new MalBool(true);
                return new MalBool(((MalVector) args.get(1)).isEmpty());
            }
        };
    }

    public static MalCallable isNil() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isTrue() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalBool bool && (boolean) bool.value())
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isFalse() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalBool bool && !(boolean)bool.value())
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isSymbol() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalSymbol)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isKeyword() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalKeyword)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isMap() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalTable)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isSequential() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalVector)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isVector() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalVector && !(args.get(1) instanceof MalList))
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isString() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalString)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isNumber() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNumber)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable isFunction() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalCallable)
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }

    public static MalCallable contains() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (((MalTable) args.get(1)).containsKey(args.get(2)))
                    return new MalBool(true);
                return new MalBool(false);
            }
        };
    }
}
