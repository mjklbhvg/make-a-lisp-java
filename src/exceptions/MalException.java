package exceptions;

import types.MalType;

public class MalException extends Throwable {
    private MalType value;

    public MalException(MalType value) {
        this.value = value;
    }

    public MalType getValue() {
        return value;
    }
}
