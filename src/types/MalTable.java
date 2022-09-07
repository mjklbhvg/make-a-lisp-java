package types;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import exceptions.MalParserException;

import java.util.HashMap;

public class MalTable extends MalType implements MalContainer {

    private MalType cachedKey;

    private HashMap<MalType, MalType> table;

    public MalTable() {
        table = new HashMap<>();
        cachedKey = null;
    }

    public MalType get(MalType key) {
        return table.get(key);
    }

    public void put(MalType key, MalType value) {
        table.put(key, value);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        for (MalType key : table.keySet()) {
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
        for (MalType key : table.keySet()) {
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
            table.put(cachedKey, data);
            cachedKey = null;
        }
    }

    @Override
    public MalType checkComplete() throws MalParserException {
        if (cachedKey != null)
            throw new MalParserException("key without value '" + cachedKey + "'");
        return this;
    }

    @Override
    public MalType evalType(MalEnvironment environment) throws MalExecutionException {
        MalTable evaluatedTable = new MalTable();
        for (MalType key : table.keySet())
            evaluatedTable.put(key, get(key).eval(environment));
        return evaluatedTable;
    }
}
