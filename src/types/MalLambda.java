package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;

import java.util.ArrayList;

public class MalLambda extends MalCallable {

    private ArrayList<String> argumentSymbols;
    private MalEnvironment closureEnv;
    private MalType body;

    public MalLambda(ArrayList<String> argumentSymbols, MalType body, MalEnvironment closureEnv) {
        this.argumentSymbols = argumentSymbols;
        this.body = body;
        this.closureEnv = closureEnv;
    }

    @Override
    public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException, TCO {
        MalEnvironment e = new MalEnvironment(closureEnv);

        // TODO: variadic args
        if (args.size() - 1 != argumentSymbols.size())
            throw new MalExecutionException(this + " expected " + argumentSymbols.size() + " argument(s), not " + (args.size() - 1));

        for (int i = 0; i < argumentSymbols.size(); i++) {
            e.set(argumentSymbols.get(i), args.get(i + 1));
        }
        throw new TCO(body, e);
    }

}
