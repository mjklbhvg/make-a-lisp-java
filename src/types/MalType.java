package types;

import environment.MalEnvironment;
import exceptions.MalException;
import mal.TCO;

public abstract class MalType {
     public abstract MalType evalType(MalEnvironment environment) throws TCO, MalException;

     public final MalType eval(MalEnvironment environment) throws MalException {
          MalType t = this;
          MalEnvironment e = environment;
          try {
               while (true) {
                    try {
                         return t.evalType(e);
                    } catch (TCO tailCall) {
                         t = tailCall.evalNext;
                         e = tailCall.nextEnvironment;
                    }
               }
          } catch (ClassCastException exception) {
               throw new MalException(new MalString(exception.getMessage()));
          }
     }

     public String rawString() {
          return toString();
     }

     public Object value() {
          return this;
     }
}
