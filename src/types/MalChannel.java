package types;

import exceptions.MalExecutionException;
import mal.Evaluator;

import java.util.concurrent.ArrayBlockingQueue;

public class MalChannel implements MalType {

    private ArrayBlockingQueue<MalType> fifo;

    public MalChannel() {
        fifo = new ArrayBlockingQueue<>(1024);
    }
    @Override
    public MalType eval(Evaluator evaluator) throws MalExecutionException {
        return this;
    }

    public MalType take() {
        try {
            return fifo.take();
            // TODO: an atom will be mutable, copy them here
        } catch (InterruptedException e) {
            return new MalNil();
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

    @Override
    public String rawString() {
        return toString();
    }

    public String toString() {
        return "#<channel>";
    }
}
