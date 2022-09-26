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
            return fifo.take();
            // TODO: an atom will be mutable, copy them here
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
