package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import exceptions.MalParserException;
import mal.Parser;
import mal.Reader;
import types.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Util {
    public static MalCallable count() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil)
                    return new MalNumber(0);
                return new MalNumber(((MalVector) args.get(1)).size());
            }
        };
    }

    public static MalCallable list() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
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
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalAtom(args.get(1));
            }
        };
    }

    public static MalCallable deref() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return ((MalAtom) args.get(1)).dereference();
            }
        };
    }

    public static MalCallable resetAtom() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                ((MalAtom) args.get(1)).setReference(args.get(2));
                return args.get(2);
            }
        };
    }

    public static MalCallable swap() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList fnargs = new MalList();
                fnargs.add(args.get(0));
                fnargs.add(((MalAtom) args.get(1)).dereference());
                for (int i = 3; i < args.size(); i++)
                    fnargs.add(args.get(i));
                MalType executor = new MalType() {
                    @Override
                    public MalType evalType(MalEnvironment environment, MalType caller) throws MalException {
                        return ((MalCallable) args.get(2)).execute(fnargs, null, this);
                    }
                };

                ((MalAtom) args.get(1)).setReference(executor.eval(null));
                return ((MalAtom) args.get(1)).dereference();
            }
        };
    }

    public static MalCallable prettyPrint() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                for (int i = 1; i < args.size(); i++) {
                    System.out.print(args.get(i).prettyPrint());
                    if (i != args.size() - 1)
                        System.out.print(' ');
                }
                System.out.println();
                return MalNil.NIL;
            }
        };
    }

    public static MalCallable println() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                for (int i = 1; i < args.size(); i++) {
                    System.out.print(args.get(i).toString());
                }
                System.out.println();
                return MalNil.NIL;
            }
        };
    }

    public static MalCallable prettyString() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.size(); i++) {
                    str.append(args.get(i).prettyPrint());
                    if (i != args.size() - 1)
                        str.append(' ');
                }
                return new MalString(str.toString());
            }
        };
    }

    public static MalCallable string() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.size(); i++) {
                    str.append(args.get(i).toString());
                }
                return new MalString(str.toString());
            }
        };
    }

    public static MalCallable readString() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                Reader r = new Reader(((MalString) args.get(1)).value());
                Parser p = new Parser(r);
                try {
                    MalList ast = p.getAST();
                    if (ast.size() == 1)
                        return ast.get(0);
                    return ast;
                } catch (MalParserException e) {
                    throw new MalException(new MalString("Error parsing string "+((MalString) args.get(1)).value()));
                }
            }
        };
    }

    public static MalCallable slurp() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                try {
                    return new MalString(Files.readString(Path.of(((MalString) args.get(1)).value())));
                } catch (IOException e) {
                    throw new MalException(new MalString("IO Error reading file: "+e));
                }
            }
        };
    }

    public static MalCallable cons() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList newList = new MalList();
                newList.add(args.get(1));
                for (int i = 0; i < ((MalVector) args.get(2)).size(); i++)
                    newList.add(((MalVector) args.get(2)).get(i));
                return newList;
            }
        };
    }

    public static MalCallable concat() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList newList = new MalList();
                for (int i = 1; i < args.size(); i++)
                    newList.addAll((MalVector) args.get(i));
                return newList;
            }
        };
    }

    public static MalCallable vec() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalList list) {
                    MalVector vec = new MalVector();
                    vec.addAll(list);
                    return vec;
                }
                return args.get(1);
            }
        };
    }

    public static MalCallable nth() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                int index = (int) ((MalNumber) args.get(2)).value();
                if (index >= ((MalVector) args.get(1)).size() || index < 0)
                    throw new MalException(new MalString("list index out of bounds: len: " + ((MalVector) args.get(1)).size()
                    + " index: "+ args.get(2)));
                return ((MalVector) args.get(1)).get(index);
            }
        };
    }

    public static MalCallable first() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil || ((MalVector) args.get(1)).size() == 0)
                    return MalNil.NIL;
                return ((MalVector) args.get(1)).get(0);
            }
        };
    }

    public static MalCallable rest() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList newList = new MalList();
                if (args.get(1) instanceof MalNil)
                    return newList;

                for (int i = 1; i < ((MalVector) args.get(1)).size(); i++)
                    newList.add(((MalVector) args.get(1)).get(i));
                return newList;
            }
        };
    }

    public static MalCallable apply() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (!(args.get(args.size() - 1) instanceof MalVector))
                    throw new MalException(new MalString("Last argument to apply must be a vector or list"));

                MalList argumentList = new MalList();
                for (int i = 1; i < args.size() - 1; i++)
                    argumentList.add(args.get(i));
                argumentList.addAll((MalVector) args.get(args.size() - 1));
              //  System.out.println("apply: new call with args: "+argumentList);

                return ((MalCallable) args.get(1)).execute(argumentList, environment, caller);
            }
        };
    }

    public static MalCallable map() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList results = new MalList();
                for (int i = 0; i < ((MalVector) args.get(2)).size(); i++) {

                    MalList fnargs = new MalList();
                    fnargs.add(args.get(0));
                    fnargs.add(((MalVector) args.get(2)).get(i));
                    MalType executor = new MalType() {
                        @Override
                        public MalType evalType(MalEnvironment environment, MalType caller) throws MalException {
                            return ((MalCallable) args.get(1)).execute(fnargs, environment, this);
                        }
                    };
                    results.add(executor.eval(environment));
                }
                // (get (assoc (assoc {"a" 1 } "b" 2) "c" 3) "a")
                return results;
            }
        };
    }

    public static MalCallable symbol() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return new MalSymbol(((MalString) args.get(1)).value());
            }
        };
    }

    public static MalCallable keyword() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalKeyword kw)
                    return kw;
                return new MalKeyword(((MalString) args.get(1)).value());
            }
        };
    }

    public static MalCallable vector() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalVector v = new MalVector();
                for (int i = 1; i < args.size(); i++)
                    v.add(args.get(i));
                return v;
            }
        };
    }

    public static MalCallable hashMap() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalTable t = new MalTable();
                for (int i = 1; i < args.size(); i += 2)
                    t.put((MalString) args.get(i), args.get(i + 1));
                return t;
            }
        };
    }

    public static MalCallable assoc() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.size() % 2 != 0)
                    throw new MalException(new MalString("assoc needs an even amount of types to merge with a map"));
                MalTable t = new MalTable();
                t.addAll((MalTable) args.get(1));
                for (int i = 2; i < args.size(); i += 2)
                    t.put((MalString) args.get(i), args.get(i + 1));
                return t;
            }
        };
    }

    public static MalCallable dissoc() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                HashSet<String> toRemove = new HashSet<>();
                for (int i = 2; i < args.size(); i++) {
                    toRemove.add(((MalString) args.get(i)).value());
                }
                MalTable newTable = new MalTable();
                for (MalString key : ((MalTable) args.get(1)).getKeys()) {
                    if (!toRemove.contains(key.value()))
                        newTable.put(key, ((MalTable) args.get(1)).get(key));
                }
                return newTable;
            }
        };
    }

    public static MalCallable get() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil)
                    return MalNil.NIL;
                MalType value = ((MalTable) args.get(1)).get((MalString) args.get(2));
                if (value == null)
                    value = MalNil.NIL;
                return value;
            }
        };
    }

    public static MalCallable keys() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList l = new MalList();
                for (MalType t : ((MalTable) args.get(1)).getKeys())
                    l.add(t);
                return l;
            }
        };
    }

    public static MalCallable vals() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalList l = new MalList();
                for (MalType t : ((MalTable) args.get(1)).getValues())
                    l.add(t);
                return l;
            }
        };
    }

    public static MalCallable readline() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.size() > 1)
                    System.out.print(((MalString) args.get(1)).value());
                try {
                    String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    if (line == null)
                        return MalNil.NIL;
                    return new MalString(line);
                } catch (IOException e) {
                    throw new MalException(new MalString(e.getMessage()));
                }
            }
        };
    }

    public static MalCallable timeMillis() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) {
                return new MalNumber(System.currentTimeMillis());
            }
        };
    }

    public static MalCallable meta() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                throw new MalException(new MalString("not implemented"));
            }
        };
    }

    public static MalCallable withMeta() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                throw new MalException(new MalString("not implemented"));
            }
        };
    }

    public static MalCallable seq() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalNil)
                    return args.get(1);
                if (args.get(1) instanceof MalString str && !(args.get(1) instanceof MalKeyword)) {
                    if (str.value().isEmpty())
                        return MalNil.NIL;
                    MalList l = new MalList();
                    for (char c : str.value().toCharArray())
                        l.add(new MalString("" + c));
                    return l;
                }
                if (args.get(1) instanceof MalList)
                    return args.get(1);
                MalList l = new MalList();
                l.add(((MalVector) args.get(1)).subArray(0, ((MalVector) args.get(1)).size()));
                return l;
            }
        };
    }

    public static MalCallable conj() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                if (args.get(1) instanceof MalList list) {
                    MalList newList = new MalList();
                    for (int i = args.size() - 1; i > 1; i--)
                        newList.add(args.get(i));
                    newList.add(list.subArray(0, list.size()));
                    return newList;
                }
                MalVector vector = (MalVector) args.get(1);
                MalVector newVector = new MalVector();
                newVector.add(vector.subArray(0, vector.size()));
                newVector.add(args.subArray(2, args.size()));
                return newVector;
            }
        };
    }
}
