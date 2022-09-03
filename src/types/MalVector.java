package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

import java.util.ArrayList;

public class MalVector extends ArrayList<MalType> implements MalType, MalContainer {

    public MalVector() {super();}

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size(); i++) {
            str.append(get(i).toString());
            if (i < size() - 1)
                str.append(" ");
        }
        str.append(']');
        return str.toString();
    }

    @Override
    public MalType eval(MalEnvironment e) throws MalExecutionException {
        for (int i = 0; i < size(); i++)
            set(i, get(i).eval(e));
        return this;
    }

    @Override
    public void store(MalType data) {
        add(data);
    }

    @Override
    public MalContainer checkComplete() {
        return this;
    }
}
