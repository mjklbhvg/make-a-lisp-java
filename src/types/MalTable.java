package types;

import environment.MalEnvironment;
import exceptions.MalException;
import exceptions.MalParserException;

import java.util.Collection;
import java.util.HashMap;

public class MalTable extends MalType implements MalContainer {

    private MalString cachedKey;

    private HashMap<String, MalType> store;
    private HashMap<String, MalString> keys;

    public MalTable() {
        store = new HashMap<>();
        keys = new HashMap<>();
        cachedKey = null;
    }

    public boolean equals(Object o) {
        if (!(o instanceof MalTable t))
            return false;
        return t.store.equals(store);
    }

    public MalType get(MalString key) {
        return store.get(key.value());
    }

    public void put(MalString key, MalType value) {
        store.put(key.value(), value);
        keys.put(key.value(), key);
    }

    public String prettyPrint() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        for (String key : store.keySet()) {
            str.append("\n\t");
            str.append(keys.get(key).prettyPrint());
            str.append(" -> ");
            // If the pretty printed value is multiple lines long, indent all of them
            str.append(store.get(key).prettyPrint().replaceAll("\n", "\n\t\t"));
            str.append(",");
        }
        str.append("\n}");
        return str.toString();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('{');
        for (String key : store.keySet()) {
            str.append(keys.get(key));
            str.append(' ');
            str.append(store.get(key));
            str.append(' ');
        }
        if (str.length() > 1)
            str.deleteCharAt(str.length() - 1);
        str.append('}');
        return str.toString();
    }
    @Override
    public void store(MalType data) throws MalParserException {
        if (cachedKey == null) {
            if (!(data instanceof MalString str))
                throw new MalParserException("hash-map keys have to be either a string or a keyword");
            cachedKey = str;
        } else {
            store.put(cachedKey.value(), data);
            keys.put(cachedKey.value(), cachedKey);
            cachedKey = null;
        }
    }

    public Collection<MalString> getKeys(){
        return keys.values();
    }

    public Collection<MalType> getValues() {
        return store.values();
    }

    public boolean containsKey(MalString key) {
        return keys.containsValue(key);
    }

    public void addAll(MalTable t) {
        for (MalString str : t.keys.values()) {
            put(str, t.get(str));
        }
    }

    @Override
    public MalType checkComplete() throws MalParserException {
        if (cachedKey != null)
            throw new MalParserException("key without value '" + cachedKey + "'");
        return this;
    }

    @Override
    public MalType evalType(MalEnvironment environment, MalType caller) throws MalException {
        MalTable evaluatedTable = new MalTable();
        for (MalString key : keys.values())
            evaluatedTable.put(key, store.get(key.value()).eval(environment));
        return evaluatedTable;
    }
}
