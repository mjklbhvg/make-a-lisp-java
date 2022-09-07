package types;

import exceptions.MalParserException;

public interface MalContainer {
     void store(MalType data);
     MalType checkComplete() throws MalParserException;
}
