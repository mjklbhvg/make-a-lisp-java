package environment;

import builtin.Env;
import builtin.Numeric;
import exceptions.MalExecutionException;
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

    public void put(String key, MalType value, boolean protect) {
        store.put(key, value);
        if (protect)
            protectedWords.add(key);
    }

    // prevent the special built-ins like let*, ... from being shadowed or overwritten
    public void set(String key, MalType value) throws MalExecutionException {
        if (protectedWords.contains(key))
            throw new MalExecutionException("attempt to shadow or overwrite special builtin " + key);
        store.put(key, value);
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
        base.put("let*", Env.addEnvironment(), true);
        base.put("def!", Env.modifyEnvironment(), true);
        return base;
    }
}
