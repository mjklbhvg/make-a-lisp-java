package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.MalCallable;
import types.MalList;
import types.MalNumber;
import types.MalType;

public class Numeric {
    public static MalCallable add() {
        return new MalCallable(new Class[]{MalNumber.class}, null, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                double result = 0;
                for (int i = 1; i < args.size(); i++)
                    result += ((MalNumber) args.get(i)).value();
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable subtract() {
        return new MalCallable(new Class[]{MalNumber.class}, null, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                double result = ((MalNumber) args.get(1)).value();

                // (- 3) should evaluate to -3
                if (args.size() == 2)
                    return new MalNumber(-result);

                for (int i = 2; i < args.size(); i++)
                    result -= ((MalNumber) args.get(i)).value();
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable multiply() {
        return new MalCallable(new Class[]{MalNumber.class}, null, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                double result = 1;
                for (int i = 1; i < args.size(); i++)
                    result *= ((MalNumber) args.get(i)).value();
                return new MalNumber(result);
            }
        };
    }

    public static MalCallable divide() {
        return new MalCallable(new Class[]{MalNumber.class}, null, true) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException{
                double result = ((MalNumber) args.get(1)).value();

                // (/ 2) should evaluate to 0.5
                if (args.size() == 2)
                    return new MalNumber(1 / result);

                for (int i = 2; i < args.size(); i++) {
                    if (((MalNumber) args.get(i)).value() == 0)
                        throw new MalExecutionException("division by zero");
                    result /= ((MalNumber) args.get(i)).value();
                }
                return new MalNumber(result);
            }
        };
    }
}
