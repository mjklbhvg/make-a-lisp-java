package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;
import types.*;

import java.util.ArrayList;

public class Function {
    public static MalSpecial lambda() {
        return new MalSpecial(new Class[]{MalVector.class, MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                MalVector symbolList = (MalVector) args.get(1);

                ArrayList<String> arguments = new ArrayList<>();
                for (int i = 0; i < symbolList.size(); i++) {
                    if (!(symbolList.get(i) instanceof MalSymbol symbol))
                        throw new MalExecutionException("expected variable name, not " + symbolList.get(i));
                    arguments.add(symbol.value());
                }
                return new MalLambda(arguments, args.get(2), environment);
            }
        };
    }

    public static MalCallable eval() {
        return new MalCallable(new Class[]{MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws TCO {
                throw new TCO(args.get(1), environment.getRoot());
            }
        };
    }

    public static MalSpecial macroexpand() {
        return new MalSpecial(new Class[]{MalList.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (!MalMacro.isMacroCall(args.get(1), environment))
                    throw new MalExecutionException("macroexpand expects a macro call");
                MalList macroCall = (MalList) args.get(1);
                macroCall.set(0, macroCall.get(0).eval(environment));
                try {
                    return ((MalMacro) macroCall.get(0)).expand(macroCall, environment);
                } catch (TCO tco) {
                    return tco.evalNext;
                }
            }
        };
    }
}
