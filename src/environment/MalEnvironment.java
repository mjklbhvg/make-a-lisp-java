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
import java.util.Objects;

public class MalEnvironment implements Cloneable {
    private MalEnvironment outerEnv;
    private HashMap<String, MalType> store;
    public MalEnvironment(MalEnvironment outerEnv) {
        this.outerEnv = outerEnv;
        store = new HashMap<>();
    }

    public MalEnvironment getRoot() {
        if (outerEnv == null)
            return this;
        return outerEnv.getRoot();
    }

    public void set(String key, MalType value) {
        store.put(key, value);
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

    public static MalEnvironment getBaseEnvironment() {
        MalEnvironment base = new MalEnvironment(null);

        base.store.put("*host-language*", new MalString("Java"));

        base.store.put("list?", Predicate.isList());
        base.store.put("empty?", Predicate.isEmpty());
        base.store.put("atom?", Predicate.isAtom());
        base.store.put("nil?", Predicate.isNil());
        base.store.put("symbol?", Predicate.isSymbol());
        base.store.put("true?", Predicate.isTrue());
        base.store.put("false?", Predicate.isFalse());
        base.store.put("keyword?", Predicate.isKeyword());
        base.store.put("vector?", Predicate.isVector());
        base.store.put("sequential?", Predicate.isSequential());
        base.store.put("map?", Predicate.isMap());
        base.store.put("contains?", Predicate.contains());
        base.store.put("fn?", Predicate.isFunction());
        base.store.put("string?", Predicate.isString());
        base.store.put("number?", Predicate.isNumber());


        base.store.put("+", Numeric.add());
        base.store.put("-", Numeric.subtract());
        base.store.put("*", Numeric.multiply());
        base.store.put("/", Numeric.divide());

        base.store.put("=", Conditional.equals());
        base.store.put("<", Conditional.less());
        base.store.put("<=", Conditional.lessEq());
        base.store.put(">", Conditional.greater());
        base.store.put(">=", Conditional.greaterEq());

        base.store.put("prn", Util.prettyPrint());
        base.store.put("println", Util.println());
        base.store.put("pr-str", Util.prettyString());
        base.store.put("str", Util.string());
        base.store.put("list", Util.list());
        base.store.put("count", Util.count());
        base.store.put("read-string", Util.readString());
        base.store.put("slurp", Util.slurp());
        base.store.put("atom", Util.atom());
        base.store.put("deref", Util.deref());
        base.store.put("reset!", Util.resetAtom());
        base.store.put("swap!", Util.swap());
        base.store.put("cons", Util.cons());
        base.store.put("concat", Util.concat());
        base.store.put("vec", Util.vec());
        base.store.put("macroexpand", Function.macroexpand());
        base.store.put("nth", Util.nth());
        base.store.put("first", Util.first());
        base.store.put("rest", Util.rest());
        base.store.put("apply", Util.apply());
        base.store.put("map", Util.map());
        base.store.put("symbol", Util.symbol());
        base.store.put("keyword", Util.keyword());
        base.store.put("vector", Util.vector());
        base.store.put("hash-map", Util.hashMap());
        base.store.put("assoc", Util.assoc());
        base.store.put("dissoc", Util.dissoc());
        base.store.put("get", Util.get());
        base.store.put("keys", Util.keys());
        base.store.put("vals", Util.vals());
        base.store.put("readline", Util.readline());
        base.store.put("time-ms", Util.timeMillis());
        base.store.put("meta", Util.meta());
        base.store.put("with-meta", Util.withMeta());
        base.store.put("seq", Util.seq());
        base.store.put("conj", Util.conj());

        base.store.put("let*", Env.addEnvironment());
        base.store.put("def!", Env.modifyEnvironment());
        base.store.put("defmacro!", Env.defineMacro());
        base.store.put("if", Conditional.malIF());
        base.store.put("do", Conditional.malDO());
        base.store.put("fn*", Function.lambda());
        base.store.put("eval", Function.eval());
        base.store.put("quote", Quote.quote());
        base.store.put("quasiquote", Quote.quasiquote());
        base.store.put("try*", TryCatch.tryCatch());
        base.store.put("catch*", TryCatch.invalidCatch());
        base.store.put("throw", TryCatch.throwType());

        base.store.put("chan", Channel.createChannel());
        base.store.put("<-", Channel.receive());
        base.store.put("->", Channel.send());
        base.store.put("run", Channel.run());

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
