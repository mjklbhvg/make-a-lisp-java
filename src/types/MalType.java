package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.Evaluator;

public interface MalType {
     MalType eval(MalEnvironment e, Evaluator evaluator) throws MalExecutionException;

     String rawString();
}
