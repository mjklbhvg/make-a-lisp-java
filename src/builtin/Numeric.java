package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.MalCallable;
import types.MalList;
import types.MalNumber;
import types.MalType;

public class Numeric {
    public static MalCallable add() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() <= 1)
                    throw new MalExecutionException("add needs more arguments");

                double result = 0;
                for (int i = 1; i < args.size(); i++) {
                    MalNumber.assertIsNumber(args.get(i));
                    result += ((MalNumber) args.get(i)).value();
                }
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable subtract() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() <= 1)
                    throw new MalExecutionException("subtract needs more arguments");

                MalNumber.assertIsNumber(args.get(1));
                double result = ((MalNumber) args.get(1)).value();

                // (- 3) should evaluate to -3
                if (args.size() == 2)
                    return new MalNumber(-result);

                for (int i = 2; i < args.size(); i++) {
                    MalNumber.assertIsNumber(args.get(i));
                    result -= ((MalNumber) args.get(i)).value();
                }
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable multiply() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() <= 1)
                    throw new MalExecutionException("multiply needs more arguments");

                double result = 1;
                for (int i = 1; i < args.size(); i++) {
                    MalNumber.assertIsNumber(args.get(i));
                    result *= ((MalNumber) args.get(i)).value();
                }
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable divide() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() <= 1)
                    throw new MalExecutionException("divide needs more arguments");

                MalNumber.assertIsNumber(args.get(1));
                double result = ((MalNumber) args.get(1)).value();

                // (/ 2) should evaluate to 0.5
                if (args.size() == 2)
                    return new MalNumber(1 / result);

                for (int i = 2; i < args.size(); i++) {
                    MalNumber.assertIsNumber(args.get(i));
                    result /= ((MalNumber) args.get(i)).value();
                }
                return new MalNumber(result);
            }
        };
    }
}
