package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;

public interface MalType {
     MalType eval(MalEnvironment e) throws MalExecutionException;
}
