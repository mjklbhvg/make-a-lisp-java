package types;

import exceptions.MalExecutionException;
import mal.Evaluator;

public interface MalType {
     MalType eval(Evaluator evaluator) throws MalExecutionException;

     String rawString();
}
