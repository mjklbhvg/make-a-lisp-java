package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import mal.TCO;

public abstract class MalType {
     public abstract MalType evalType(MalEnvironment environment) throws MalExecutionException, TCO;

     public final MalType eval(MalEnvironment environment) throws MalExecutionException {
          MalType t = this;
          MalEnvironment e = environment;
          while (true) {
               try {
                    return t.evalType(e);
               } catch (TCO tailCall) {
                    t = tailCall.evalNext;
                    e = tailCall.nextEnvironment;
               }
          }
     }

     public String rawString() {
          return toString();
     }
}
