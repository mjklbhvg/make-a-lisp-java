package types;

import environment.MalEnvironment;
import exceptions.MalException;

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
    public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
        MalEnvironment e = new MalEnvironment(closureEnv);

        for (int i = 0; i < argumentSymbols.size(); i++) {
            if (argumentSymbols.get(i).equals("&")) {
                if (argumentSymbols.size() != i + 2)
                    throw new MalException(new MalString("Expected exactly one symbol name after &"));
                String name = argumentSymbols.get(i + 1);
               MalList argumentList = new MalList();
               for (i += 1; i < args.size(); i++) {
                   argumentList.add(args.get(i));
               }
               e.set(name, argumentList);
               break;
            } else {
                if ((i + 1) >= args.size())
                    throw new MalException(new MalString(this + " expected " + argumentSymbols.size() + " argument(s), not " + (args.size() - 1)));
                e.set(argumentSymbols.get(i), args.get(i + 1));
              //  System.out.println("lambda added "+argumentSymbols.get(i)+" ("+args.get(i + 1)+") to env");
            }
        }
       // System.out.println("Lambda call '"+body);
        caller.evalNext(body, e);
        return null;
    }
}
