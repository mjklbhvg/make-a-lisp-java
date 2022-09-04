package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Evaluator;

public class MalList extends MalVector implements MalType {
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

    @Override
    public MalType eval(MalEnvironment e, Evaluator evaluator) throws MalExecutionException {
        if (size() == 0)
            return this;

        // Don't call anything as the main AST list:
        // otherwise '+ 1 1' would evaluate to 2 instead of '+ 1 1'

        // This is needed to e.g. make a 'let*' symbol evaluate itself
        // to the let* special
        MalList evaluatedList = new MalList();
        evaluatedList.setMainAST(mainAST);
        evaluatedList.add(get(0).eval(e, evaluator));
        for (int i = 1; i < size(); i++)
            evaluatedList.add(get(i));
        if (!mainAST && evaluatedList.get(0) instanceof MalSpecial spec) {
            return spec.execute(evaluatedList, e, evaluator);
        }

        for (int i = 1; i < size(); i++)
            evaluatedList.set(i, get(i).eval(e, evaluator));

        if (!mainAST && evaluatedList.get(0) instanceof MalCallable func) {
          //  System.out.println("function call!!");
            return func.execute(evaluatedList, e, evaluator);
        }
        return evaluatedList;
    }

    @Override
    public MalContainer checkComplete() {
        return this;
    }
}
