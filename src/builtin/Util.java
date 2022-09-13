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
        return new MalCallable(new Class[]{MalVector.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalNumber(((MalVector) args.get(1)).size());
            }
        };
    }

    public static MalCallable isEmpty() {
        return new MalCallable(new Class[]{MalVector.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(((MalVector) args.get(1)).isEmpty());
            }
        };
    }

    public static MalCallable isList() {
        return new MalCallable(new Class[]{MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(args.get(1) instanceof MalList);
            }
        };
    }

    public static MalCallable list() {
        return new MalCallable(new Class[]{}, new Class[]{MalType.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                MalList l = new MalList();
                for (int i = 1; i < args.size(); i++)
                    l.add(args.get(i));
                return l;
            }
        };
    }

    public static MalCallable atom() {
        return new MalCallable(new Class[]{MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalAtom(args.get(1));
            }
        };
    }

    public static MalCallable isAtom() {
        return new MalCallable(new Class[]{MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return new MalBool(args.get(1) instanceof MalAtom);
            }
        };
    }

    public static MalCallable deref() {
        return new MalCallable(new Class[]{MalAtom.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return ((MalAtom) args.get(1)).value();
            }
        };
    }

    public static MalCallable resetAtom() {
        return new MalCallable(new Class[]{MalAtom.class, MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                ((MalAtom) args.get(1)).setValue(args.get(2));
                return args.get(2);
            }
        };
    }

    public static MalCallable swap() {
        return new MalCallable(new Class[]{MalAtom.class, MalCallable.class}, new Class[]{MalType.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                MalList fnargs = new MalList();
                fnargs.add(args.get(0));
                fnargs.add(((MalAtom) args.get(1)).value());
                for (int i = 3; i < args.size(); i++)
                    fnargs.add(args.get(i));
                MalType executor = new MalType() {
                    @Override
                    public MalType evalType(MalEnvironment environment) throws MalExecutionException, TCO {
                        return ((MalCallable) args.get(2)).execute(fnargs, environment);
                    }
                };

                ((MalAtom) args.get(1)).setValue(executor.eval(environment));
                return ((MalAtom) args.get(1)).value();
            }
        };
    }

    public static MalCallable print() {
        return new MalCallable(new Class[]{}, new Class[]{MalType.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
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
        return new MalCallable(new Class[]{}, new Class[]{MalType.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
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
        return new MalCallable(new Class[]{}, new Class[]{MalType.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
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
        return new MalCallable(new Class[]{}, new Class[]{MalType.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
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
        return new MalCallable(new Class[]{MalString.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                Reader r = new Reader(((MalString) args.get(1)).value());
                Parser p = new Parser(r);
                try {
                    MalList ast = p.getAST();
                    if (ast.size() == 1)
                        return ast.get(0);
                    return ast;
                } catch (MalParserException e) {
                    throw new MalExecutionException("Error parsing string "+((MalString) args.get(1)).value());
                }
            }
        };
    }

    public static MalCallable slurp() {
        return new MalCallable(new Class[]{MalString.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                try {
                    return new MalString(Files.readString(Path.of(((MalString) args.get(1)).value())));
                } catch (IOException e) {
                    throw new MalExecutionException("IO Error reading file: "+e);
                }
            }
        };
    }

    public static MalCallable cons() {
        return new MalCallable(new Class[]{MalType.class, MalList.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                MalList newList = new MalList();
                newList.add(args.get(1));
                for (int i = 0; i < ((MalList) args.get(2)).size(); i++)
                    newList.add(((MalList) args.get(2)).get(i));
                return newList;
            }
        };
    }

    public static MalCallable concat() {
        return new MalCallable(new Class[]{}, new Class[]{MalList.class}, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                MalList newList = new MalList();
                for (int i = 1; i < args.size(); i++) {
                    for (int j = 0; j < ((MalList) args.get(i)).size(); j++)
                        newList.add(((MalList) args.get(i)).get(j));
                }
                return newList;
            }
        };
    }
}
