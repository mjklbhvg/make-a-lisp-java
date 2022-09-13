package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;

public abstract class MalCallable extends MalType {
    public abstract MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException, TCO;

    private Class<MalType>[] argTypes, optionalArgTypes;
    private boolean var, noTypeCheck;

    public MalCallable(Class<MalType>[] argTypes, Class<MalType>[] optionalArgTypes, boolean var) {
        this.var = var;
        this.argTypes = argTypes;
        this.optionalArgTypes = optionalArgTypes;
        noTypeCheck = false;
    }

     public MalCallable() {
        noTypeCheck = true;
     }

    public MalType execute(MalList args, MalEnvironment env) throws MalExecutionException, TCO {
        if (noTypeCheck)
            return executeChecked(args, env);

        if (args.size() == 0)
            throw new MalExecutionException("Invalid argument list passed to function " + this);

        // First, check all non optional arguments
        int index = 1;
        Class<MalType> expectedType = null;
        for (Class<MalType> argType : argTypes) {
            if (index >= args.size()) {
                throw new MalExecutionException(args.get(0) + " expected at least " + argTypes.length
                        + " argument(s), but got " + (args.size() - 1) + ".");
            }
            expectedType = argType;

            if (!(expectedType.isInstance(args.get(index))))
                throw new MalExecutionException(args.get(0) + ", Argument " + index + ": expected \033[3m"
                        + expectedType.getSimpleName() + "\033[m not \033[3m"
                        + args.get(index).getClass().getSimpleName() + "\033[m.");
            index++;
        }

        // all args have been checked
        if (index == args.size())
            return executeChecked(args, env);

        // if there are optional args check if the remaining args match
        if (optionalArgTypes != null) {
            for (Class<MalType> optionalArgType : optionalArgTypes) {
                expectedType = optionalArgType;

                if (!(expectedType.isInstance(args.get(index))))
                    throw new MalExecutionException(args.get(0) + ", Argument " + index + ": expected \033[3m"
                            + expectedType.getSimpleName() + "\033[m not \033[3m"
                            + args.get(index).getClass().getSimpleName() + "\033[m.");
                index++;
                if (index == args.size()) {
                    // not an error, because these args are optional
                    return executeChecked(args, env);
                }
            }
        }

        // there are still arguments left - this is an error if var is false
        if (!var) {
            int maxArgs = argTypes.length;
            if (optionalArgTypes != null)
                maxArgs += optionalArgTypes.length;
            throw new MalExecutionException(args.get(0) + " expected at most " + maxArgs
                    + " argument(s), but got " + (args.size() - 1) + ".");
        }

        // check that the remaining arguments all have the same type as the last expected type
        if (expectedType == null)
            throw new RuntimeException("Function declared with variable args, but no args specified.\n" +
                    "THIS IS AN INTERPRETER BUG");

        for (;index < args.size(); index++){
            if (!(expectedType.isInstance(args.get(index))))
                throw new MalExecutionException(args.get(0)+", Argument " + index + ": expected \033[3m"
                        + expectedType.getSimpleName() + "\033[m not \033[3m"
                        + args.get(index).getClass().getSimpleName() + "\033[m.");
        }
        return executeChecked(args, env);
    }
    @Override
    public MalType evalType(MalEnvironment environment) {
        return this;
    }

    public String toString() {
        return "#<function>";
    }
}
