package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Evaluator;

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
    protected MalType execute(MalList args, MalEnvironment env, Evaluator evaluator) throws MalExecutionException {
            MalEnvironment newEnv = new MalEnvironment(closureEnv);

            if (args.size() - 1 != argumentSymbols.size())
                throw new MalExecutionException("lambda expected " + argumentSymbols.size() + " argument(s), not " + (args.size() - 1));

            for (int i = 0; i < argumentSymbols.size(); i++)
                newEnv.set(argumentSymbols.get(i), args.get(i + 1));

            evaluator.scheduleTask(body, newEnv);
            return null;
    }

}
