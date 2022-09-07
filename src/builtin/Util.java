package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import exceptions.MalParserException;
import mal.Parser;
import mal.Reader;
import mal.TCO;
import types.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Util {
    public static MalCallable count() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2 || !(args.get(1) instanceof MalVector vec))
                    throw new MalExecutionException("count needs exactly one list argument");
                return new MalNumber(vec.size());
            }
        };
    }

    public static MalCallable isEmpty() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2 || !(args.get(1) instanceof MalVector vec))
                    throw new MalExecutionException("empty needs exactly one list argument");
                return new MalBool(vec.isEmpty());
            }
        };
    }

    public static MalCallable isList() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2)
                    throw new MalExecutionException("is_list needs exactly one argument");
                return new MalBool(args.get(1) instanceof MalList);
            }
        };
    }

    public static MalCallable list() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) {
                MalList l = new MalList();
                for (int i = 1; i < args.size(); i++)
                    l.add(args.get(i));
                return l;
            }
        };
    }

    public static MalCallable atom() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2)
                    throw new MalExecutionException("atom expects 1 argument");
                return new MalAtom(args.get(1));
            }
        };
    }

    public static MalCallable isAtom() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2)
                    throw new MalExecutionException("is-atom expects 1 argument");
                return new MalBool(args.get(1) instanceof MalAtom);
            }
        };
    }

    public static MalCallable deref() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2
                        || !(args.get(1) instanceof MalAtom atom))
                    throw new MalExecutionException("deref expects 1 atom as an argument");
                return atom.value();
            }
        };
    }

    public static MalCallable resetAtom() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 3
                        || !(args.get(1) instanceof MalAtom atom))
                    throw new MalExecutionException("reset expects 1 atom and 1 type as arguments");
                atom.setValue(args.get(2));
                return args.get(2);
            }
        };
    }

    public static MalCallable swap() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() < 3
                        || !(args.get(1) instanceof MalAtom atom)
                        || !(args.get(2) instanceof MalCallable func))
                    throw new MalExecutionException("swap expects an atom and a function + optional arguments");

                MalList fnargs = new MalList();
                fnargs.add(args.get(0));
                fnargs.add(atom.value());
                for (int i = 3; i < args.size(); i++)
                    fnargs.add(args.get(i));
                MalType executor = new MalType() {
                    @Override
                    public MalType evalType(MalEnvironment environment) throws MalExecutionException, TCO {
                        return func.execute(fnargs, environment);
                    }
                };

                atom.setValue(executor.eval(environment));
                return atom.value();
            }
        };
    }

    public static MalCallable print() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) {
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
            public MalType execute(MalList args, MalEnvironment environment) {
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
            public MalType execute(MalList args, MalEnvironment environment) {
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
            public MalType execute(MalList args, MalEnvironment environment) {
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

    public static MalCallable readString() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2
                        || !(args.get(1) instanceof MalString str))
                    throw new MalExecutionException("read string expects a string as an argument");
                Reader r = new Reader(str.value());
                Parser p = new Parser(r);
                try {
                    MalList ast = p.getAST();
                    if (ast.size() == 1)
                        return ast.get(0);
                    return ast;
                } catch (MalParserException e) {
                    throw new MalExecutionException("Error parsing string "+str);
                }
            }
        };
    }

    public static MalCallable slurp() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2
                        || !(args.get(1) instanceof MalString filename))
                    throw new MalExecutionException("slurp expects a filename as an argument");
                try {
                    return new MalString(Files.readString(Path.of(filename.value())));
                } catch (IOException e) {
                    throw new MalExecutionException("IO Error reading file: "+e);
                }
            }
        };
    }
}
