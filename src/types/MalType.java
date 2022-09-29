package types;

import environment.MalEnvironment;
import exceptions.MalException;

public abstract class MalType {
     public abstract MalType evalType(MalEnvironment environment, MalType caller) throws MalException;

     private enum Action {EVAL, EXEC, RETURN}

     private Action action = Action.RETURN;

     private MalEnvironment newEnv;
     private MalType toEval;

     private MalList args;


     public void evalNext(MalType toEval, MalEnvironment newEnv) {
          action = Action.EVAL;
          this.newEnv = newEnv;
          this.toEval = toEval;
     }

     public void executeNext(MalCallable toExecute, MalEnvironment newEnv, MalList args) {
          action = Action.EXEC;
          toEval = toExecute;
          this.newEnv = newEnv;
          this.args = args;
     }

     public final MalType eval(MalEnvironment environment) throws MalException {
          try {
               MalType result = evalType(environment, this);
               while (true) {
                    Action tmp = action;
                    action = Action.RETURN; // reset action for next iteration
                    switch (tmp) {
                         case EVAL ->
                                 result = toEval.evalType(newEnv, this);
                         case EXEC ->
                                 result = ((MalCallable) toEval).execute(args, newEnv, this);
                         default -> {
                              if (result == null)
                                   System.out.println("disaster olloolololol "+action);
                              return result;
                         }
                    }
               }
          } catch (ClassCastException exception) {
               throw new MalException(new MalString(exception.getMessage()));
          }
     }

     public String prettyPrint() {
          return toString();
     }
}
