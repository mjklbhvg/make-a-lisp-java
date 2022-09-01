package types;

import java.util.ArrayList;

public class MalList implements MalType {

    private final ArrayList<MalType> list;
    private boolean mainAST = false;

    public MalList() {
        list = new ArrayList<MalType>();
    }
    public void add(MalType t) {
        list.add(t);
    }

    public void setMainAST(boolean mainAST) {this.mainAST = mainAST;}

    public String toString() {
        StringBuilder str = new StringBuilder();
        if (!mainAST) str.append('(');
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i).toString());
            if (i < list.size() - 1)
                str.append(' ');
        }
        if (!mainAST) str.append(')');
        return str.toString();
    }
}
