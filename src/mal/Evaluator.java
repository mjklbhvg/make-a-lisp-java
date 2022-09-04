package mal;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.MalType;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Evaluator {

    // statt exceptions:
    // der evaluator ist immer ein argument zu eval und execute usw.
    // der evaluator hat eine queue an Eval Tasks
    // Will irgendeine Funktion optimieren kann sie dem Evaluator einen neuen Task geben und dann null returnen
    // Der evaluator wird in einem loop die tasks abarbeiten :^)
    // da die tasks dank integrierten envs fest sind kann ein evaluator wenn er bock hat auch mehrere taks parallell
    // ausfuhren (go function) builtin

    // Can i design channels?
    // (def! channel (chan))
    // (go (fn* () (println channel))
    // (-> channel "hello") // -> is a special that doesn't eval the channel
    // -- prints hello
    // MalChannel evals to whatever is in the fifo, might block

    private ConcurrentLinkedQueue<EvalTask> tasks;

    public Evaluator() {
        tasks = new ConcurrentLinkedQueue<>();
    }

    private static class EvalTask {
        MalEnvironment environment;
        MalType toEval;

        public EvalTask(MalType toEval, MalEnvironment e) {
            environment = e;
            this.toEval = toEval;
        }
    }

    public void scheduleTask(MalType toEval, MalEnvironment e) {
        //System.out.println("Scheduled evaluation of "+toEval);
        tasks.add(new EvalTask(toEval, e));
    }

    public MalType evaluate(MalType type, MalEnvironment env) throws MalExecutionException {
        MalType result = null;
        scheduleTask(type, env);
        while (!tasks.isEmpty()) {
            EvalTask next = tasks.remove();
            result = next.toEval.eval(next.environment, this);
        }
        return result;
    }


}
