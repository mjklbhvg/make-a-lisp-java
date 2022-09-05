package environment;

import builtin.*;
import exceptions.MalExecutionException;
import exceptions.MalParserException;
import mal.Evaluator;
import mal.Parser;
import mal.Reader;
import types.MalType;

import java.util.HashMap;
import java.util.HashSet;

public class MalEnvironment implements Cloneable {
    private MalEnvironment outerEnv;
    private HashMap<String, MalType> store;
    private static HashSet<String> protectedWords = new HashSet<>();

    public MalEnvironment(MalEnvironment outerEnv) {
        this.outerEnv = outerEnv;
        store = new HashMap<>();
    }

    protected void put(String key, MalType value, boolean protect) {
        store.put(key, value);
        if (protect)
            protectedWords.add(key);
    }

    // prevent the special built-ins like let*, ... from being shadowed or overwritten
    public void set(String key, MalType value) throws MalExecutionException {
        if (protectedWords.contains(key))
            throw new MalExecutionException("attempt to shadow or overwrite special builtin " + key);
        put(key, value, false);
      //  System.out.println("Set "+key+" to "+value);
    }

    public MalType get(String key) throws MalExecutionException {
        if (store.containsKey(key))
            return store.get(key);
        if (outerEnv == null)
            throw new MalExecutionException("symbol " + key + " is not defined");
        return outerEnv.get(key);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static MalEnvironment getBaseEnvironment() {
        MalEnvironment base = new MalEnvironment(null);
        base.put("+", Numeric.add(), false);
        base.put("-", Numeric.subtract(), false);
        base.put("*", Numeric.multiply(), false);
        base.put("/", Numeric.divide(), false);
        base.put("=", Conditional.equals(), false);
        base.put("<", Conditional.less(), false);
        base.put("<=", Conditional.lessEq(), false);
        base.put(">", Conditional.greater(), false);
        base.put(">=", Conditional.greaterEq(), false);
        base.put("prn", Util.print(), false);
        base.put("println", Util.printRaw(), false);
        base.put("pr-str", Util.string(), false);
        base.put("str", Util.stringRaw(), false);
        base.put("list", Util.list(), false);
        base.put("list?", Util.isList(), false);
        base.put("empty?", Util.isEmpty(), false);
        base.put("count", Util.count(), false);

        base.put("let*", Env.addEnvironment(), true);
        base.put("def!", Env.modifyEnvironment(), true);
        base.put("if", Conditional.malIF(), true);
        base.put("do", Conditional.malDO(), true);
        base.put("fn*", Function.lambda(), true);

        base.put("chan", Channel.createChannel(), true);
        base.put("<-", Channel.receive(), true);
        base.put("->", Channel.send(), true);
        base.put("run", Channel.run(), true);

        Evaluator initEval = new Evaluator(base);
        // TODO: load this from a file
        String initCode = "(def! not (fn* (a) (if a false true)))";

            try {
                MalType ast = new Parser(new Reader(initCode)).getAST();
                initEval.nextTask(ast);
                initEval.evaluate();
            } catch (MalParserException | MalExecutionException e) {
                throw new RuntimeException(e);
            }
        return base;
    }
}
