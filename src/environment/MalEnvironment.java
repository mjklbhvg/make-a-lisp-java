package environment;

import builtin.Numeric;
import types.MalCallable;

import java.util.HashMap;

public class MalEnvironment extends HashMap<String, MalCallable> {
    private MalEnvironment outerEnv;

    public MalEnvironment(MalEnvironment outerEnv) {
        this.outerEnv = outerEnv;
    }

    public MalEnvironment getOuterEnv() {
        return outerEnv;
    }

    public static MalEnvironment getBaseEnvironment() {
        MalEnvironment base = new MalEnvironment(null);
        base.put("+", Numeric.add());
        base.put("-", Numeric.subtract());
        base.put("*", Numeric.multiply());
        base.put("/", Numeric.divide());
        return base;
    }
}
