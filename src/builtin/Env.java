package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;
import types.*;

public class Env {
   public static MalSpecial addEnvironment() {
        return new MalSpecial(new Class[]{MalVector.class, MalType.class}, null, false) {
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException, TCO {
                MalVector bindings = (MalVector) args.get(1);

                if (bindings.size() % 2 != 0)
                    throw new MalExecutionException("mismatched keys and values in let*");

                MalEnvironment newEnv = new MalEnvironment(environment);
                for (int i = 0; i < bindings.size(); i += 2) {
                    if (!(bindings.get(i) instanceof MalSymbol sym))
                        throw new MalExecutionException("expected a variable name, not" + bindings.get(i));
                    newEnv.set(sym.value(), bindings.get(i + 1).eval(newEnv));
                }
                throw new TCO(args.get(2), newEnv);
            }
        };
    }

    public static MalSpecial modifyEnvironment() {
        return new MalSpecial(new Class[]{MalSymbol.class, MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                MalType value = args.get(2).eval(environment);
                environment.set(((MalSymbol) args.get(1)).value(), value);
                return value;
            }
        };
    }

    public static MalSpecial defineMacro() {
       return new MalSpecial(new Class[]{MalSymbol.class, MalType.class}, null, false) {
           @Override
           public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
               MalType value = args.get(2).eval(environment);
               if (!(value instanceof MalLambda lambda))
                   throw new MalExecutionException("defmacro must bind a lambda");
               MalMacro macro = new MalMacro(lambda);
               environment.set(((MalSymbol) args.get(1)).value(), macro);
               return macro;
           }
       };
    }
}
