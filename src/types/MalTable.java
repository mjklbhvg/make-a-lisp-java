package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import exceptions.MalParserException;

import java.util.HashMap;

public class MalTable extends HashMap<MalType, MalType> implements MalType, MalContainer {

    private MalType cachedKey;

    public MalTable() {
        super();
        cachedKey = null;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        for (MalType key : keySet()) {
            str.append("\n\t");
            str.append(key.toString());
            str.append(" ⟶ ");
            str.append(get(key).toString());
            str.append(",");
        }
        str.append("\n   }");
        return str.toString();
    }

    @Override
    public void store(MalType data) {
        if (cachedKey == null) {
                cachedKey = data;
        } else {
            put(cachedKey, data);
            cachedKey = null;
        }
    }

    @Override
    public MalContainer checkComplete() throws MalParserException {
        if (cachedKey != null)
            throw new MalParserException("key without value '" + cachedKey + "'");
        return this;
    }

    @Override
    public MalType eval(MalEnvironment e) throws MalExecutionException {
        for (MalType key : keySet())
            put(key, get(key).eval(e));
        return this;
    }
}
