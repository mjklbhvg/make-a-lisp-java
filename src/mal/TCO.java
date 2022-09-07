package mal;

import environment.MalEnvironment;
import types.MalType;

public class TCO extends Throwable {
     public MalType evalNext;
     public MalEnvironment nextEnvironment;

     public TCO(MalType evalNext, MalEnvironment nextEnvironment) {
         this.evalNext = evalNext;
         this.nextEnvironment = nextEnvironment;
     }
}
