package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

import java.util.ArrayList;

public class MalList extends ArrayList<MalType> implements MalType {
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
                str.append(", ");
        }
        if (!mainAST) str.append(')');
        return str.toString();
    }

    @Override
    public MalType eval(MalEnvironment e) throws MalExecutionException {
        if (size() == 0)
            return this;
        for (int i = 0; i < size(); i++)
            set(i, get(i).eval(e));
        // Don't call anything with the main AST list:
        // otherwise '+ 1 1' would evaluate to 2 instead of '+ 1 1'
        if (!mainAST && get(0) instanceof MalCallable) {
            return ((MalCallable) (get(0))).execute(this);
        }
        return this;
    }
}
