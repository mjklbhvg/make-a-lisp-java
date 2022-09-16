package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;


public class MalList extends MalVector {
    private boolean mainAST = false;

    public MalList() {
        super();
    }

    public void setMainAST(boolean mainAST) {this.mainAST = mainAST;}

    public String toString() {
        StringBuilder str = new StringBuilder();
        if (!mainAST) str.append('(');
        for (int i = 0; i < size(); i++) {
            str.append(get(i).toString());
            if (i < size() - 1)
                str.append(" ");
        }
        if (!mainAST) str.append(')');
        return str.toString();
    }

    public String rawString() {
        StringBuilder str = new StringBuilder();
        if (!mainAST) str.append('(');
        for (int i = 0; i < size(); i++) {
            str.append(get(i).rawString());
            if (i < size() - 1)
                str.append(" ");
        }
        if (!mainAST) str.append(')');
        return str.toString();
    }

    public MalType evalType(MalEnvironment environment) throws MalExecutionException, TCO {
        if (size() == 0)
            return this;

        // Don't call anything as the main AST list:
        // otherwise '+ 1 1' would evaluate to 2 instead of '+ 1 1'

        // This is needed to e.g. make a 'let*' symbol evaluate itself
        // to the let* special
        MalList evaluatedList = new MalList();
        evaluatedList.setMainAST(mainAST);
        evaluatedList.add(get(0).eval(environment));
        for (int i = 1; i < size(); i++)
            evaluatedList.add(get(i));

        if (!mainAST && evaluatedList.get(0) instanceof MalMacro macro) {
            evaluatedList = macro.expand(evaluatedList, environment);
            if (evaluatedList.size() == 0)
                return evaluatedList;
            evaluatedList.set(0, evaluatedList.get(0).eval(environment));
        }

        if (!mainAST && evaluatedList.get(0) instanceof MalSpecial spec) {
            return spec.execute(evaluatedList, environment);
        }

        for (int i = 1; i < size(); i++)
            evaluatedList.set(i, get(i).eval(environment));

        if (mainAST)
            return evaluatedList;

        if (evaluatedList.get(0) instanceof MalCallable func) {
            return func.execute(evaluatedList, environment);
        }
        else
            throw new MalExecutionException(evaluatedList.get(0) + " can't be called as a function");
    }

    @Override
    public MalType checkComplete() {
        return this;
    }
}
