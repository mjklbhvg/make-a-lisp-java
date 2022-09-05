package builtin;

import exceptions.MalExecutionException;
import mal.Evaluator;
import types.*;

public class Util {
    public static MalCallable count() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) throws MalExecutionException {
                if (args.size() != 2 || !(args.get(1) instanceof MalVector vec))
                    throw new MalExecutionException("count needs exactly one list argument");
                return new MalNumber(vec.size());
            }
        };
    }

    public static MalCallable isEmpty() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) throws MalExecutionException {
                if (args.size() != 2 || !(args.get(1) instanceof MalVector vec))
                    throw new MalExecutionException("empty needs exactly one list argument");
                return new MalBool(vec.isEmpty());
            }
        };
    }

    public static MalCallable isList() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) throws MalExecutionException {
                if (args.size() != 2)
                    throw new MalExecutionException("is_list needs exactly one argument");
                return new MalBool(args.get(1) instanceof MalList);
            }
        };
    }

    public static MalCallable list() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) {
                MalList l = new MalList();
                for (int i = 1; i < args.size(); i++)
                    l.add(args.get(i));
                return l;
            }
        };
    }

    public static MalCallable print() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) {
                for (int i = 1; i < args.size(); i++) {
                    System.out.print(args.get(i));
                    if (i != args.size() - 1)
                        System.out.print(' ');
                }
                System.out.println();
                return new MalNil();
            }
        };
    }

    public static MalCallable printRaw() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) {
                for (int i = 1; i < args.size(); i++) {
                    System.out.print(args.get(i).rawString());
                    if (i != args.size() - 1)
                        System.out.print(' ');
                }
                System.out.println();
                return new MalNil();
            }
        };
    }

    public static MalCallable string() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) {
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.size(); i++) {
                    str.append(args.get(i).toString());
                    if (i != args.size() - 1)
                        str.append(' ');
                }
                return new MalString(str.toString());
            }
        };
    }

    public static MalCallable stringRaw() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, Evaluator evaluator) {
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.size(); i++) {
                    str.append(args.get(i).rawString());
                    if (i != args.size() - 1)
                        str.append(' ');
                }
                return new MalString(str.toString());
            }
        };
    }
}
