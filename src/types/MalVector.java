package types;

import environment.MalEnvironment;
import exceptions.MalException;
import mal.TCO;

import java.util.ArrayList;

public class MalVector extends MalType implements MalContainer {

    private ArrayList<MalType> list;
    public MalVector() {
        list = new ArrayList<>();
    }

    public int size() {
        return list.size();
    }

    public void set(int i, MalType t) {
        list.set(i, t);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void add(MalType t) {
        list.add(t);
    }

    public void addAll(MalVector v) {
        list.addAll(v.list);
    }

    public MalType get(int i) throws MalException {
        if (i < 0 || i >= list.size())
            throw new MalException(new MalString("Index " + i + " out of bounds for length "+list.size()+"."));
        return list.get(i);
    }

    public boolean equals(Object o) {
        if (!(o instanceof MalVector vec))
            return false;

        if (size() != vec.size())
            return false;

        try {
            for (int i = 0; i < size(); i++) {
                if (!list.get(i).equals(vec.get(i)))
                    return false;
            }
        } catch (MalException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size(); i++) {
            str.append(list.get(i).toString());
            if (i < size() - 1)
                str.append(" ");
        }
        str.append(']');
        return str.toString();
    }

    @Override
    public MalType evalType(MalEnvironment environment) throws TCO, MalException {
        MalVector evaluatedVector = new MalVector();
        for (int i = 0; i < size(); i++)
            evaluatedVector.add(get(i).eval(environment));
        return evaluatedVector;
    }

    @Override
    public void store(MalType data) {
        add(data);
    }

    @Override
    public MalType checkComplete() {
        return this;
    }
}
