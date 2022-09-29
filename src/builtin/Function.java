package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

import java.util.ArrayList;

public class Function {
    public static MalSpecial lambda = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalVector symbolList = (MalVector) args.get(1);

                ArrayList<String> arguments = new ArrayList<>();
                for (int i = 0; i < symbolList.size(); i++) {
                    if (!(symbolList.get(i) instanceof MalSymbol symbol))
                        throw new MalException(new MalString("expected variable name, not " + symbolList.get(i)));
                    arguments.add(symbol.value());
                }
                return new MalLambda(arguments, args.get(2), environment);
            }
        };

    public static MalCallable eval = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                caller.evalNext(args.get(1), environment.getRoot());
                return null;
            }
        };

    public static MalSpecial macroexpand = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalMacro m = MalMacro.isMacroCall(args.get(1), environment);
                if (m == null)
                    throw new MalException(new MalString("macroexpand expects a macro call"));
                return m.expand((MalList) args.get(1), environment);

            }
        };
}
