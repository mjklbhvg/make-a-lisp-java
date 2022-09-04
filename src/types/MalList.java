package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

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

    @Override
    public MalType eval(MalEnvironment e) throws MalExecutionException {
        if (size() == 0)
            return this;

        // Don't call anything as the main AST list:
        // otherwise '+ 1 1' would evaluate to 2 instead of '+ 1 1'

        // This is needed to e.g. make a 'let*' symbol evaluate itself
        // to the let* special
        set(0, get(0).eval(e));
        if (!mainAST && get(0) instanceof MalSpecial spec) {
            return spec.execute(this, e);
        }

        for (int i = 1; i < size(); i++)
            set(i, get(i).eval(e));

        if (!mainAST && get(0) instanceof MalCallable func) {
            return func.execute(this);
        }
        return this;
    }

    @Override
    public MalContainer checkComplete() {
        return this;
    }
}
