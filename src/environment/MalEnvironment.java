package environment;

import builtin.*;
import exceptions.MalException;
import exceptions.MalParserException;
import mal.Parser;
import mal.Reader;
import types.MalString;
import types.MalType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

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
    public void set(String key, MalType value) throws MalException {
        if (protectedWords.contains(key))
            throw new MalException(new MalString("attempt to shadow or overwrite special builtin " + key));
        put(key, value, false);
      //  System.out.println("Set "+key+" to "+value);
    }

    public MalType get(String key) throws MalException {
        if (store.containsKey(key))
            return store.get(key);
        if (outerEnv == null)
            throw new MalException(new MalString("symbol " + key + " is not defined"));
        return outerEnv.get(key);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void dump() {
        if (outerEnv == null)
            return;
        for (String key : store.keySet())
            System.out.println(key + " -> "+store.get(key));
    }

    public static MalEnvironment getBaseEnvironment() {
        MalEnvironment base = new MalEnvironment(null);

        base.put("list?", Predicate.isList(), false);
        base.put("empty?", Predicate.isEmpty(), false);
        base.put("atom?", Predicate.isAtom(), false);
        base.put("nil?", Predicate.isNil(), false);
        base.put("symbol?", Predicate.isSymbol(), false);
        base.put("true?", Predicate.isTrue(), false);
        base.put("false?", Predicate.isFalse(), false);
        base.put("symbol?", Predicate.isSymbol(), false);
        base.put("keyword?", Predicate.isKeyword(), false);
        base.put("vector?", Predicate.isVector(), false);
        base.put("sequential?", Predicate.isSequential(), false);
        base.put("map?", Predicate.isMap(), false);
        base.put("contains?", Predicate.contains(), false);


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
        base.put("count", Util.count(), false);
        base.put("read-string", Util.readString(), false);
        base.put("slurp", Util.slurp(), false);
        base.put("atom", Util.atom(), false);
        base.put("deref", Util.deref(), false);
        base.put("reset!", Util.resetAtom(), false);
        base.put("swap!", Util.swap(), false);
        base.put("cons", Util.cons(), false);
        base.put("concat", Util.concat(), false);
        base.put("vec", Util.vec(), false);
        base.put("macroexpand", Function.macroexpand(), false);
        base.put("nth", Util.nth(), false);
        base.put("first", Util.first(), false);
        base.put("rest", Util.rest(), false);
        base.put("apply", Util.apply(), false);
        base.put("map", Util.map(), false);
        base.put("symbol", Util.symbol(), false);
        base.put("keyword", Util.keyword(), false);
        base.put("vector", Util.vector(), false);
        base.put("hash-map", Util.hashMap(), false);
        base.put("assoc", Util.assoc(), false);
        base.put("dissoc", Util.dissoc(), false);
        base.put("get", Util.get(), false);
        base.put("keys", Util.keys(), false);
        base.put("vals", Util.vals(), false);

        base.put("let*", Env.addEnvironment(), true);
        base.put("def!", Env.modifyEnvironment(), true);
        base.put("defmacro!", Env.defineMacro(), true);
        base.put("if", Conditional.malIF(), true);
        base.put("do", Conditional.malDO(), true);
        base.put("fn*", Function.lambda(), true);
        base.put("eval", Function.eval(), true);
        base.put("quote", Quote.quote(), true);
        base.put("quasiquote", Quote.quasiquote(), true);
        base.put("try*", TryCatch.tryCatch(), true);
        base.put("catch*", TryCatch.invalidCatch(), true);
        base.put("throw", TryCatch.throwType(), true);

        base.put("chan", Channel.createChannel(), true);
        base.put("<-", Channel.receive(), true);
        base.put("->", Channel.send(), true);
        base.put("run", Channel.run(), true);

        String initCode;
        try {
            initCode = "(do "
                    + Files.readString(Path.of(Objects.requireNonNull(MalEnvironment.class.getClassLoader().getResource("environment/init.mal")).getPath()))
                    + " )";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
                MalType ast = new Parser(new Reader(initCode)).getAST();
                ast.eval(base);
        } catch (MalParserException | MalException e) {
            throw new RuntimeException(e);
        }
        return base;
    }
}
