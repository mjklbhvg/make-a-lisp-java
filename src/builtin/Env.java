package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Env {
   public static MalSpecial addEnvironment = new MalSpecial() {
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalVector bindings = (MalVector) args.get(1);

                if (bindings.size() % 2 != 0)
                    throw new MalException(new MalString("mismatched keys and values in let*"));

                MalEnvironment newEnv = new MalEnvironment(environment);
                for (int i = 0; i < bindings.size(); i += 2) {
                    if (!(bindings.get(i) instanceof MalSymbol sym))
                        throw new MalException(new MalString("expected a variable name, not" + bindings.get(i)));
                    newEnv.set(sym.value(), bindings.get(i + 1).eval(newEnv));
                }
                caller.evalNext(args.get(2), newEnv);
                return null;
            }
        };

    public static MalSpecial modifyEnvironment = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                MalType value = args.get(2).eval(environment);
                environment.set(((MalSymbol) args.get(1)).value(), value);
                return value;
            }
        };

    public static MalSpecial defineMacro = new MalSpecial() {
           @Override
           public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
               MalType value = args.get(2).eval(environment);
               if (!(value instanceof MalLambda lambda))
                   throw new MalException(new MalString("defmacro must bind a lambda"));
               MalMacro macro = new MalMacro(lambda);
               environment.set(((MalSymbol) args.get(1)).value(), macro);
               return macro;
           }
       };
}
