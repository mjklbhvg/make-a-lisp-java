package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Numeric {
    public static MalCallable add() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                double result = 0;
                for (int i = 1; i < args.size(); i++)
                    result += (double) (args.get(i)).value();
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable subtract() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                double result = (double) (args.get(1)).value();

                // (- 3) should evaluate to -3
                if (args.size() == 2)
                    return new MalNumber(-result);

                for (int i = 2; i < args.size(); i++)
                    result -= (double) (args.get(i)).value();
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable multiply() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                double result = 1;
                for (int i = 1; i < args.size(); i++)
                    result *= (double) (args.get(i)).value();
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable divide() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                double result = (double) (args.get(1)).value();

                // (/ 2) should evaluate to 0.5
                if (args.size() == 2) {
                    if (result == 0)
                        throw new MalException(new MalString("division by zero"));
                    return new MalNumber(1 / result);
                }

                for (int i = 2; i < args.size(); i++) {
                    if ((double) (args.get(i)).value() == 0)
                        throw new MalException(new MalString("division by zero"));
                    result /= (double) (args.get(i)).value();
                }
                return new MalNumber(result);
            }
        };
    }
}
