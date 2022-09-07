package environment;

import builtin.*;
import exceptions.MalExecutionException;
import exceptions.MalParserException;
import mal.Parser;
import mal.Reader;
import types.MalType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public MalEnvironment getRoot() {
        if (outerEnv == null)
            return this;
        return outerEnv.getRoot();
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
        base.put("read-string", Util.readString(), false);
        base.put("slurp", Util.slurp(), false);
        base.put("atom?", Util.isAtom(), false);
        base.put("atom", Util.atom(), false);
        base.put("deref", Util.deref(), false);
        base.put("reset!", Util.resetAtom(), false);
        base.put("swap!", Util.swap(), false);

        base.put("let*", Env.addEnvironment(), true);
        base.put("def!", Env.modifyEnvironment(), true);
        base.put("if", Conditional.malIF(), true);
        base.put("do", Conditional.malDO(), true);
        base.put("fn*", Function.lambda(), true);
        base.put("eval", Function.eval(), true);

        base.put("chan", Channel.createChannel(), true);
        base.put("<-", Channel.receive(), true);
        base.put("->", Channel.send(), true);
        base.put("run", Channel.run(), true);

        String initCode;
        try {
            initCode = "(do "
                    + Files.readString(Path.of(MalEnvironment.class.getClassLoader().getResource("environment/init.mal").getPath()))
                    + " )";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
                MalType ast = new Parser(new Reader(initCode)).getAST();
                ast.eval(base);
            } catch (MalParserException | MalExecutionException e) {
                throw new RuntimeException(e);
            }
        return base;
    }
}
