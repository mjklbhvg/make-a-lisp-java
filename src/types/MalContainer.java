package types;

import exceptions.MalParserException;

public interface MalContainer {
     void store(MalType data) throws MalParserException;
     MalType checkComplete() throws MalParserException;
}
