package types;

import environment.MalEnvironment;
import exceptions.MalException;
import exceptions.MalParserException;

import java.util.Collection;
import java.util.HashMap;

public class MalTable extends MalType implements MalContainer {

    private MalType cachedKey;

    private HashMap<Object, MalType> keyToValue;
    private HashMap<Object, MalType> keyToKey;

    public MalTable() {
        keyToKey = new HashMap<>();
        keyToValue = new HashMap<>();
        cachedKey = null;
    }

    public boolean equals(Object o) {
        if (!(o instanceof MalTable t))
            return false;
        return t.keyToValue.equals(keyToValue);
    }

    public MalType get(MalType key) {
        return keyToValue.get(key.value());
    }

    public void put(MalType key, MalType value) {
        keyToValue.put(key.value(), value);
        keyToKey.put(key.value(), key);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        for (Object key : keyToKey.keySet()) {
            str.append("\n\t");
            str.append(keyToKey.get(key).toString());
            str.append(" ⟶ ");
            str.append(keyToValue.get(key).toString());
            str.append(",");
        }
        str.append("\n   }");
        return str.toString();
    }

    @Override
    public String rawString() {
        StringBuilder str = new StringBuilder();
        str.append('{');
        for (Object key : keyToKey.keySet()) {
            str.append(keyToKey.get(key).rawString());
            str.append(' ');
            str.append(keyToValue.get(key).rawString());
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
            keyToKey.put(cachedKey.value(), cachedKey);
            keyToValue.put(cachedKey.value(), data);
            cachedKey = null;
        }
    }

    public Collection<MalType> getKeys(){
        return keyToKey.values();
    }

    public Collection<MalType> getValues() {
        return keyToValue.values();
    }

    public boolean containsKey(MalType key) {
        return keyToKey.containsKey(key.value());
    }

    public void addAll(MalTable t) {
        keyToKey.putAll(t.keyToKey);
        keyToValue.putAll(t.keyToValue);
    }

    @Override
    public MalType checkComplete() throws MalParserException {
        if (cachedKey != null)
            throw new MalParserException("key without value '" + cachedKey + "'");
        return this;
    }

    @Override
    public MalType evalType(MalEnvironment environment) throws MalException {
        MalTable evaluatedTable = new MalTable();
        for (MalType key : keyToKey.values())
            evaluatedTable.put(key, get(key).eval(environment));
        return evaluatedTable;
    }
}
