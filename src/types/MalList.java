package types;

import environment.MalEnvironment;
import exceptions.MalException;


public class MalList extends MalVector {
    private boolean mainAST = false;

    public MalList() {
        super();
        openChar = '(';
        closeChar = ')';
    }

    public void setMainAST(boolean mainAST) {
        this.mainAST = mainAST;
    }

    public MalType evalType(MalEnvironment environment, MalType caller) throws MalException {
        if (size() == 0)
            return this;

        // This is needed to e.g. make a 'let*' symbol evaluate itself
        // to the let* special
        MalList evaluatedList = new MalList();
        evaluatedList.setMainAST(mainAST);
        evaluatedList.add(get(0).eval(environment));
        evaluatedList.add(subArray(1, size()));

        // Don't call anything as the main AST list:
        // otherwise '+ 1 1' would evaluate to 2 instead of '+ 1 1'
        if (!mainAST && evaluatedList.get(0) instanceof MalMacro macro) {
            MalType expanded = macro.expand(evaluatedList, environment);
            if (!(expanded instanceof MalList expandedList)) {
                caller.evalNext(expanded, environment);
                return null;
            }
            evaluatedList.clear();
            evaluatedList.addAll(expandedList);

            if (evaluatedList.size() == 0)
                return evaluatedList;
            evaluatedList.set(0, evaluatedList.get(0).eval(environment));
        }

        if (!mainAST && evaluatedList.get(0) instanceof MalSpecial spec) {
            caller.executeNext(spec, environment, evaluatedList);
            return null;
        }

        for (int i = 1; i < evaluatedList.size(); i++)
            evaluatedList.set(i, evaluatedList.get(i).eval(environment));

        if (mainAST)
            return evaluatedList;

        if (evaluatedList.get(0) instanceof MalCallable func) {
            caller.executeNext(func, environment, evaluatedList);
            return null;
        } else
            throw new MalException(new MalString(evaluatedList.get(0) + " can't be called as a function"));
    }

    @Override
    public MalType checkComplete() {
        return this;
    }
}
