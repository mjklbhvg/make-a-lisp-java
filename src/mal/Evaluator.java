package mal;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.MalType;

public class Evaluator {

    // Can i design channels?
    // (def! channel (chan))
    // (go (fn* () (println channel))
    // (-> channel "hello") // -> is a special that doesn't eval the channel
    // -- prints hello
    // MalChannel evals to whatever is in the fifo, might block

    private MalEnvironment environment;
    private MalType toEval;
    private boolean hasTask = false;

    public Evaluator(MalEnvironment environment) {
        this.environment = environment;
    }

    public void nextTask(MalType toEval) {
        this.toEval = toEval;
        hasTask = true;
    }

    public void nextTask(MalType toEval, MalEnvironment environment) {
        this.toEval = toEval;
        this.environment = environment;
        hasTask = true;
    }

    public MalEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(MalEnvironment environment) {
        this.environment = environment;
    }

    public MalType evaluate() throws MalExecutionException {
        MalType result = null;
        while (hasTask) {
            hasTask = false;
            result = toEval.eval(this);
        }
        return result;
    }


}
