package types;

import environment.MalEnvironment;

import java.util.concurrent.ArrayBlockingQueue;

public class MalChannel extends MalType {

    private ArrayBlockingQueue<MalType> fifo;

    public MalChannel() {
        fifo = new ArrayBlockingQueue<>(1024);
    }
    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) {
        return this;
    }

    public MalType take() {
        try {
            MalType t = fifo.take();
            if (t instanceof MalAtom atm)
                return new MalAtom(atm.getReference());
            return t;
        } catch (InterruptedException e) {
            return MalNil.NIL;
        }
    }

    public boolean add(MalType element) {
        try {
            fifo.add(element);
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "#<channel>";
    }
}
