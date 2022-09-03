package types;

import exceptions.MalParserException;

public interface MalContainer extends MalType {
     void store(MalType data);
     MalContainer checkComplete() throws MalParserException;
}
