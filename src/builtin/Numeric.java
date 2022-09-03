package builtin;

import exceptions.MalExecutionException;
import types.MalCallable;
import types.MalNumber;

public class Numeric {
    public static MalCallable add() {
        return (args) -> {
            if (args.size() <= 1)
                throw new MalExecutionException("+ needs more arguments");

            double result = 0;
            for (int i = 1; i < args.size(); i++) {
                MalNumber.assertIsNumber(args.get(i));
                result += ((MalNumber) args.get(i)).value();
            }
            return new MalNumber(result);
        };
    }

    public static MalCallable subtract() {
        return (args) -> {
            if (args.size() <= 1)
                throw new MalExecutionException("- needs more arguments");

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
        };
    }

    public static MalCallable multiply() {
        return (args) -> {
            if (args.size() <= 1)
                throw new MalExecutionException("* needs more arguments");

            double result = 1;
            for (int i = 1; i < args.size(); i++) {
                MalNumber.assertIsNumber(args.get(i));
                result *= ((MalNumber) args.get(i)).value();
            }
            return new MalNumber(result);
        };
    }

    public static MalCallable divide() {
        return (args) -> {
            if (args.size() <= 1)
                throw new MalExecutionException("/ needs more arguments");

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
        };
    }
}
