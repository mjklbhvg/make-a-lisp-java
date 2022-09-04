package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import exceptions.MalParserException;
import mal.Evaluator;

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
    public String rawString() {
        StringBuilder str = new StringBuilder();
        str.append('{');
        for (MalType key : keySet()) {
            str.append(key.rawString());
            str.append(' ');
            str.append(get(key).rawString());
            str.append(' ');
        }
        if (str.length() > 1)
            str.deleteCharAt(str.length() - 1);
        str.append('}');
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
    public MalType eval(MalEnvironment e, Evaluator evaluator) throws MalExecutionException {
        MalTable evaluatedTable = new MalTable();
        for (MalType key : keySet())
            evaluatedTable.put(key, get(key).eval(e, evaluator));
        return evaluatedTable;
    }
}
