package types;

public abstract class MalSpecial extends MalCallable {
    public MalSpecial(Class<MalType>[] argTypes, Class<MalType>[] optionalArgTypes, boolean var) {
        super(argTypes, optionalArgTypes, var);
    }
    public String toString() {
        return "#<special>";
    }
}
