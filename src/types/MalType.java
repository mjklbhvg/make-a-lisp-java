package types;

import environment.MalEnvironment;
import exceptions.MalException;

public abstract class MalType {
     public abstract MalType evalType(MalEnvironment environment, MalType caller) throws MalException;


     private final int EVAL = 1, EXEC = 2, EVAL_WITH_EXC = 3;

     private int action = 0;

     protected MalEnvironment newEnv, exceptionEnv;
     private MalSymbol exceptionSymbol;
     protected MalType toEval, exceptionBody;
     private MalList args;


     public void evalNext(MalType toEval, MalEnvironment newEnv) {
          action = EVAL;
          this.newEnv = newEnv;
          this.toEval = toEval;
     }

     public void catchNext(MalType toEval, MalEnvironment newEnv,
                           MalType exceptionBody, MalSymbol exceptionSym, MalEnvironment exceptionEnv) {
          action = EVAL_WITH_EXC;
          this.newEnv = newEnv;
          this.toEval = toEval;
          this.exceptionBody = exceptionBody;
          this.exceptionSymbol = exceptionSym;
          this.exceptionEnv = exceptionEnv;
     }

     public void executeNext(MalCallable toExecute, MalEnvironment newEnv, MalList args) {
          action = EXEC;
          toEval = toExecute;
          this.newEnv = newEnv;
          this.args = args;
     }

     public final MalType eval(MalEnvironment environment) throws MalException {
          try {
               MalType result = evalType(environment, this);
               while (true) {
                    int a = action;
                    action = 0; // reset action for next iteration
                    switch (a) {
                         case EVAL ->
                                 result = toEval.evalType(newEnv, this);
                         case EXEC ->
                                 result = ((MalCallable) toEval).execute(args, newEnv, this);
                         case EVAL_WITH_EXC -> {
                              try {
                                   // probably bad
                                   result = toEval.eval(newEnv);
                              } catch (MalException me) {
                                   MalEnvironment catchEnv = new MalEnvironment(exceptionEnv);
                                   catchEnv.set(exceptionSymbol.value(), me.getValue());
                                   result = exceptionBody.evalType(catchEnv, this);
                              }
                         }
                         default -> {
                              if (result == null)
                                   System.out.println("desaster olloolololol "+action);
                              return result;
                         }
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
