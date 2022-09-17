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

        for (int i = 0; i < argumentSymbols.size(); i++) {
            if (argumentSymbols.get(i).equals("&")) {
                if (argumentSymbols.size() != i + 2)
                    throw new MalExecutionException("Expected exactly one symbol name after &");
                String name = argumentSymbols.get(i + 1);
               MalList argumentList = new MalList();
               for (i += 1; i < args.size(); i++) {
                   argumentList.add(args.get(i));
               }
               e.set(name, argumentList);
               break;
            } else {
                if ((i + 1) >= args.size())
                    throw new MalExecutionException(this + " expected " + argumentSymbols.size() + " argument(s), not " + (args.size() - 1));
                e.set(argumentSymbols.get(i), args.get(i + 1));
            }
        }
        throw new TCO(body, e);
    }

}
